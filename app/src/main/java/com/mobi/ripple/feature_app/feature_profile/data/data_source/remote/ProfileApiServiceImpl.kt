package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.ApiRequest
import com.mobi.ripple.core.data.ApiResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UpdateUserProfileInfoRequest
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserProfileInfoResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserSimplePostsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.path

class ProfileApiServiceImpl(
    private val client: HttpClient
) : ProfileApiService {
    override suspend fun getUserProfilePicture(
        username: String
    ): ApiResponse<String> = ApiRequest<String> {
        client.get {
            url {
                path(AppUrls.ProfileUrls.userProfilePicture(username))
            }
        }
    }.sendRequest()

    override suspend fun uploadUserProfilePicture(
        image: ByteArray
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.submitFormWithBinaryData(
            url = AppUrls.ProfileUrls.USER_PROFILE_PICTURE,
            formData = formData {
                append("image", image, Headers.build {
                    // Mime type required
                    append(HttpHeaders.ContentType, "images/*")
                    // Filename in content disposition required
                    append(HttpHeaders.ContentDisposition, "filename=pfp_image")
                })
            }
        )
    }.sendRequest()


    override suspend fun deleteUserProfilePicture(): ApiResponse<Boolean> =
        ApiRequest<Boolean> {
            client.delete {
                url(AppUrls.ProfileUrls.USER_PROFILE_PICTURE)
            }
        }.sendRequest()

    override suspend fun getUserProfileInfo(
        username: String
    ): ApiResponse<UserProfileInfoResponse> =
        ApiRequest<UserProfileInfoResponse> {
            client.get(
                AppUrls.ProfileUrls.userProfileInfoUrl(username)
            )
        }.sendRequest()

    override suspend fun isOtherUserWithUsernameExists(username: String): ApiResponse<Boolean> =
        ApiRequest<Boolean> {
            client.get(
                AppUrls.ProfileUrls.existsOtherUserWithUsername(username)
            )
        }.sendRequest()

    override suspend fun isOtherUserWithEmailExists(email: String): ApiResponse<Boolean> =
        ApiRequest<Boolean> {
            client.get(
                AppUrls.ProfileUrls.existsOtherUserWithEmail(email)
            )
        }.sendRequest()


    override suspend fun editProfileInfo(
        updateUserProfileInfoRequest: UpdateUserProfileInfoRequest
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.ProfileUrls.UPDATE_USER) {
            setBody(updateUserProfileInfoRequest)
        }
    }.sendRequest()

    override suspend fun getSimplePosts(
        username: String,
        page: Long
    ): ApiResponse<List<UserSimplePostsResponse>> =
        ApiRequest<List<UserSimplePostsResponse>> {
            client.get {
                url {
                    path(AppUrls.ProfileUrls.userSimplePostsUrl(username))
                    appendPathSegments("simple-posts")
                    parameters.append("page", page.toString())
                }
            }
        }.sendRequest()

}

