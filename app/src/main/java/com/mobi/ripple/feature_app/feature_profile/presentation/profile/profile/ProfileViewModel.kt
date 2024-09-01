package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.config.ConstraintValues
import com.mobi.ripple.core.util.ImageUtils
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.ProfileUseCases
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.asUserProfileInfoModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.asUserProfileSimplePostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
import kotlin.math.floor

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            GlobalAppManager.storedUsername?.let { username ->
                launch {
                    val profileInfoResponse = profileUseCases.getProfileInfoUseCase(username)
                    if (profileInfoResponse.isError) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(profileInfoResponse.errorMessage))
                    } else {
                        _state.value.userProfileInfoState.value =
                            profileInfoResponse.content!!.asUserProfileInfoModel()
                    }
                }
                launch {
                    val profilePictureResponse =
                        profileUseCases.getProfilePictureUseCase(username)
                    if (profilePictureResponse.isError) {
                        if (profilePictureResponse.httpStatusCode != HttpStatusCode.NotFound) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(profilePictureResponse.errorMessage))
                        }
                    } else {
                        _state.value.userProfilePicture.value =
                            profilePictureResponse.content!!.image
                    }
                }
                launch {
                    val simplePostsResponse =
                        profileUseCases.getSimplePostsUseCase(username, state.value.page++)
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
        }
    }

    //Events triggered from Screen
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.UploadPfpRequested -> {
                viewModelScope.launch {
                    event.imageUri.toByteArray(context)?.let { imageByteArray ->
                        val compressedImageBytes = compressImage(imageByteArray, event.imageUri)
                        val result = profileUseCases
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

            is ProfileEvent.DeletePfpRequested -> {
                viewModelScope.launch {
                    val result = profileUseCases.deletePfpUseCase()
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

            is ProfileEvent.UploadPostRequested -> {
                viewModelScope.launch {
                    event.imageUri.toByteArray(context)?.let { imageByteArray ->
                        val compressedImageBytes = compressImage(imageByteArray, event.imageUri)
                        _state.value.newPostImageBytes = compressedImageBytes
                    }
                }
            }

            is ProfileEvent.SettingsClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.SettingsClicked)
                }
            }

            is ProfileEvent.EditScreenEvent.UsernameTextChanged -> {
                if (event.newText.isEmpty()) return
                viewModelScope.launch {
                    val result = profileUseCases.isOtherUserWithUsernameExists(event.newText)
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

            is ProfileEvent.EditScreenEvent.EmailTextChanged -> {
                if (event.newText.isEmpty()) return
                viewModelScope.launch {
                    val result = profileUseCases.isOtherUserWithEmailExists(event.newText)
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

            is ProfileEvent.EditScreenEvent.EditProfileInfoRequested -> {
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
                            profileUseCases.editProfileInfoUseCase(event.userModel.asUserProfileInfo())
                        if (result.isError) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.errorMessage))
                        } else {
                            if (result.content!!) {
                                _state.value.userProfileInfoState.value = event.userModel
                                _eventFlow.emit(UiEvent.UiEditScreenEvent.EditProfileSuccessful)
                            } else _eventFlow.emit(
                                UiEvent.ShowSnackbar("Changes not made, try again later")
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

        sealed class UiEditScreenEvent {
            data object EditProfileSuccessful : UiEvent()
        }
    }

    private fun compressImage(
        imageBytes: ByteArray,
        imageUri: Uri,
        maxBytes: Int? = null
    ): ByteArray {
        var maxByteSize: Int = ConstraintValues.MAX_IMAGE_SIZE_BYTES
        maxBytes?.let {
            when (it) {
                in 1..ConstraintValues.MAX_IMAGE_SIZE_BYTES -> {
                    maxByteSize = it
                }
            }
        }
        if (imageBytes.size <= maxByteSize) return imageBytes


        val overSizeCoef =
            imageBytes.size.toFloat() / maxByteSize.toFloat()

        var bitmap = ImageUtils.convertImageByteArrayToBitmap(imageBytes)

        val rotationAngle = ImageUtils.getExifRotation(context, imageUri)
        bitmap = ImageUtils.rotateBitmap(bitmap, rotationAngle)

        val compressionFractionCoef = 1f / overSizeCoef
        val qualityPercentage = (floor(compressionFractionCoef * 10) * 10f).toInt()
        val outputStream = ByteArrayOutputStream(
            (imageBytes.size.toFloat() * compressionFractionCoef + 1).toInt()
        )
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            qualityPercentage,
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
