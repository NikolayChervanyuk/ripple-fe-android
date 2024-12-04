package com.mobi.ripple.core.data.profile.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.profile.data_source.remote.dto.UserProfileInfoResponse
import com.mobi.ripple.core.data.profile.data_source.remote.dto.UserSimplePostsResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
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

    override suspend fun changeFollowingState(
        username: String
    ): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.put(AppUrls.ProfileUrls.followOrUnfollowUser(username))
    }.sendRequest()
}