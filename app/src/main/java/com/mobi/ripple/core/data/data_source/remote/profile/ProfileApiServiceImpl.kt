package com.mobi.ripple.core.data.data_source.remote.profile

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.data_source.remote.profile.dto.UserProfileInfoResponse
import com.mobi.ripple.core.data.data_source.remote.profile.dto.UserSimplePostsResponse
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiRequest
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.path

open class ProfileApiServiceImpl(
    private val client: HttpClient
): ProfileApiService {
    override suspend fun getUserProfilePicture(
        username: String
    ): ApiResponse<String> = ApiRequest<String> {
        client.get {
            url {
                path(AppUrls.ProfileUrls.userProfilePicture(username))
            }
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

    override suspend fun getSimplePosts(
        username: String,
        page: Long
    ): ApiResponse<List<UserSimplePostsResponse>> =
        ApiRequest<List<UserSimplePostsResponse>> {
            client.get {
                url {
                    path(AppUrls.ProfileUrls.userSimplePostsUrl(username))
                    parameters.append("page", page.toString())
                }
            }
        }.sendRequest()
}