package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.config.ConstraintValues
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.PersonalProfileUseCases
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.NewPostModel
import com.mobi.ripple.core.presentation.profile.model.asUserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.asUserProfileSimplePostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class PersonalProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: HttpClient,
    private val personalProfileUseCases: PersonalProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(PersonalProfileState())
    val state: StateFlow<PersonalProfileState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            GlobalAppManager.storedUsername?.let { username ->
                launch {
                    val profileInfoResponse = personalProfileUseCases.getProfileInfoUseCase(username)
                    if (profileInfoResponse.isError) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(profileInfoResponse.errorMessage))
                    } else {
                        _state.value.userProfileInfoState.value =
                            profileInfoResponse.content!!.asUserProfileInfoModel()
                    }
                }
                launch {
                    val profilePictureResponse =
                        personalProfileUseCases.getProfilePictureUseCase(username)
                    if (profilePictureResponse.isError) {
                        if (profilePictureResponse.httpStatusCode != HttpStatusCode.NotFound) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(profilePictureResponse.errorMessage))
                        }
                    } else {
                        _state.value.userProfilePicture.value =
                            profilePictureResponse.content!!.image
                    }
                }
                if (state.value.page == 0L) {
                    launch {
                        val simplePostsResponse =
                            personalProfileUseCases.getSimplePostsUseCase(username, state.value.page++)
                        if (simplePostsResponse.isError) {
                            if (simplePostsResponse.httpStatusCode != HttpStatusCode.NotFound) {
                                _eventFlow.emit(UiEvent.ShowSnackbar(simplePostsResponse.errorMessage))
                            }
                        } else {
                            _state.value.userProfileSimplePosts
                                .addAll(simplePostsResponse.content!!
                                    .map { it.asUserProfileSimplePostModel() }
                                )
                        }
                    }
                }
            } ?: GlobalAppManager.onLogout()
        }
    }

    //Events triggered from Screen
    fun onEvent(event: PersonalProfileEvent) {
        when (event) {
            is PersonalProfileEvent.UploadPfpRequested -> {
                viewModelScope.launch {
                    event.imageUri.toByteArray(context)?.let { imageByteArray ->
                        val compressedImageBytes = compressImage(imageByteArray, event.imageUri)
                        val result = personalProfileUseCases
                            .uploadPfpUseCase(compressedImageBytes)
                        if (result.isError) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.errorMessage))
                        } else {
                            if (result.content!!) {
                                _state.value.userProfilePicture.value = compressedImageBytes
                            } else {
                                _eventFlow.emit(
                                    UiEvent.ShowSnackbar("Failed to upload image, try again later")
                                )
                            }
                        }
                    }
                }
            }

            is PersonalProfileEvent.DeletePfpRequested -> {
                viewModelScope.launch {
                    val result = personalProfileUseCases.deletePfpUseCase()
                    if (result.isError) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(result.errorMessage))
                    } else {
                        if (result.content!!) {
                            _state.value.userProfilePicture.value = null
                        } else {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Failed to delete image, try again later")
                            )
                        }
                    }
                }
            }

            is PersonalProfileEvent.CreatePostRequested -> {
                viewModelScope.launch {
                    event.imageUri.toByteArray(context)?.let { imageByteArray ->
                        val compressedImageBytes = compressImage(imageByteArray, event.imageUri)
                        _state.value
                            .newPostState.value
                            .newPostModelState.value
                            .imageBytes = compressedImageBytes
                    }
                    _eventFlow.emit(UiEvent.CreatePostRequest)
                }
            }

            is PersonalProfileEvent.SettingsClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.SettingsClicked)
                }
            }

            is PersonalProfileEvent.EditScreenEvent.UsernameTextChanged -> {
                if (event.newText.isEmpty()) return
                viewModelScope.launch {
                    val result = personalProfileUseCases.isOtherUserWithUsernameExists(event.newText)
                    if (result.isError) return@launch
                    if (result.content!!) {
                        _state.value.editProfileState.value
                            .isUsernameTakenState.value = true
                    } else {
                        _state.value.editProfileState.value
                            .isUsernameTakenState.value = false
                    }
                }
            }

            is PersonalProfileEvent.EditScreenEvent.EmailTextChanged -> {
                if (event.newText.isEmpty()) return
                viewModelScope.launch {
                    val result = personalProfileUseCases.isOtherUserWithEmailExists(event.newText)
                    if (result.isError) return@launch
                    if (result.content!!) {
                        _state.value.editProfileState.value
                            .isEmailTakenState.value = true
                    } else {
                        _state.value.editProfileState.value
                            .isEmailTakenState.value = false
                    }
                }
            }

            is PersonalProfileEvent.EditScreenEvent.EditProfileInfoRequested -> {
                viewModelScope.launch {
                    if (event.userModel == state.value.userProfileInfoState.value) {
                        _eventFlow.emit(UiEvent.UiEditScreenEvent.EditProfileSuccessful)
                        return@launch
                    }

                    if (FieldValidator.isFullNameValid(event.userModel.fullName ?: "") &&
                        FieldValidator.isUsernameValid(event.userModel.userName) &&
                        FieldValidator.isEmailValid(event.userModel.email ?: "") &&
                        event.userModel.bio
                            ?.let { it.length <= ConstraintValues.MAX_BIO_LENGTH } != false
                    ) {
                        val result =
                            personalProfileUseCases.editProfileInfoUseCase(event.userModel.asUserProfileInfo())
                        if (result.isError) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.errorMessage))
                        } else {
                            if (result.content!!) {
                                GlobalAppManager.storedUsername?.let { storedUsername ->
                                    if(event.userModel.userName != storedUsername) {
                                        GlobalAppManager.onLogout()
                                    }
                                } ?: GlobalAppManager.onLogout()
                                _state.value.userProfileInfoState.value = event.userModel
                                _eventFlow.emit(UiEvent.UiEditScreenEvent.EditProfileSuccessful)
                            } else _eventFlow.emit(
                                UiEvent.ShowSnackbar("Changes not made, try again later")
                            )
                        }
                    }
                }
            }

            is PersonalProfileEvent.NewPostScreenEvent.CaptionTextChanged -> {
                _state.value
                    .newPostState.value
                    .newPostModelState.value.captionText = event.newText
            }

            is PersonalProfileEvent.NewPostScreenEvent.UploadPostRequested -> {
                viewModelScope.launch {
                    val result = personalProfileUseCases.uploadPostUseCase(
                        state.value
                            .newPostState.value
                            .newPostModelState.value
                            .asUserProfileNewPost()
                    )
                    if (result.isError) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(result.errorMessage))
                    } else {
                        if (result.content!!) {
                            launch {
                                GlobalAppManager.storedUsername?.let { username ->
                                    _state.value.page = 0
                                    val simplePostsResponse =
                                        personalProfileUseCases
                                            .getSimplePostsUseCase(username, state.value.page++)
                                    if (simplePostsResponse.isError) {
                                        _eventFlow.emit(
                                            UiEvent.ShowSnackbar(simplePostsResponse.errorMessage)
                                        )
                                    } else {
                                        _state.value.userProfileSimplePosts.clear()
                                        _state.value.userProfileSimplePosts
                                                .addAll(simplePostsResponse.content!!
                                                .map { it.asUserProfileSimplePostModel() }
                                            )
                                    }
                                }
                            }
                            _state.value
                                .userProfileInfoState.value
                                .postsCount++
                            _state.value
                                .newPostState.value
                                .newPostModelState.value =
                                NewPostModel(ByteArray(0), "")
                            _eventFlow.emit(UiEvent.UiNewPostEvent.NewPostPostedSuccessfully)
                        } else {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Post upload failed, try again later")
                            )
                        }
                    }
                }
            }
        }
    }

    //Events consumed in Screen, in LaunchedEffect for example
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object SettingsClicked : UiEvent()
        data object CreatePostRequest : UiEvent()
        sealed class UiEditScreenEvent {
            data object EditProfileSuccessful : UiEvent()
        }

        sealed class UiNewPostEvent {
            data object NewPostPostedSuccessfully : UiEvent()
        }
    }

    private fun compressImage(
        imageBytes: ByteArray,
        imageUri: Uri,
        quality: Float = 0.75f
        //maxBytes: Int = ConstraintValues.MAX_IMAGE_SIZE_BYTES
    ): ByteArray {
        val finalQuality =  if(quality < 0.2 || quality > 0.95) 0.75f else quality
//        var maxByteSize: Int = maxBytes
//        if(maxBytes > ConstraintValues.MAX_IMAGE_SIZE_BYTES){
//            maxByteSize = ConstraintValues.MAX_IMAGE_SIZE_BYTES
//        }
//        if (imageBytes.size <= maxByteSize) return imageBytes
//        val delta = 0.01f
//        val qualityCoefficient = maxByteSize / imageBytes.size.toFloat()
//        val qualityPercentage = ((qualityCoefficient - delta) * 100).toInt()

        var bitmap = BitmapUtils.convertImageByteArrayToBitmap(imageBytes)

        val rotationAngle = BitmapUtils.getExifRotation(context, imageUri)
        bitmap = BitmapUtils.rotateBitmap(bitmap, rotationAngle)

        val outputStream = ByteArrayOutputStream(
            imageBytes.size
//            (imageBytes.size.toFloat() * (qualityCoefficient + delta)).toInt()
        )
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            (finalQuality * 100).toInt(),
            outputStream
        )
        return outputStream.toByteArray()
    }

    private fun Uri.toByteArray(context: Context): ByteArray? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(this)
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream(512 * 1024)
            val buffer = ByteArray(128 * 1024)
            var len: Int
            while (inputStream?.read(buffer).also { len = it ?: -1 } != -1) {
                byteArrayOutputStream.write(buffer, 0, len)
            }
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }
}