package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.data.ApiRequest
import com.mobi.ripple.core.data.ApiResponse
import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserProfileInfoResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserProfilePictureResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserSimplePostsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.path

class ProfileApiServiceImpl(
    private val client: HttpClient
) : ProfileApiService {
    override suspend fun getUserProfilePicture(
        username: String
    ): ApiResponse<ByteArray> = ApiRequest<ByteArray> {
        client.get {

//            contentType(ContentType.Image.JPEG)
            url {
                path(AppUrls.ProfileUrls.userProfilePicture(username))
            }
        }
    }.sendRequest()

    override suspend fun getUserProfileInfo(
        username: String
    ): ApiResponse<UserProfileInfoResponse> =
        ApiRequest<UserProfileInfoResponse> {
            client.get {
                url {
                    path(AppUrls.ProfileUrls.userProfileInfoUrl(username))
                }
            }
        }.sendRequest()

    override suspend fun getSimplePosts(
        username: String,
        page: Long
    ): ApiResponse<UserSimplePostsResponse> =
        ApiRequest<UserSimplePostsResponse> {
            client.get {
                url {
                    path(AppUrls.ProfileUrls.userSimplePostsUrl(username))
                    appendPathSegments("simple-posts")
                    parameters.append("page", page.toString())
                }
            }
        }.sendRequest()

}