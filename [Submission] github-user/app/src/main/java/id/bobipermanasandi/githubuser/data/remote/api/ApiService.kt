package id.bobipermanasandi.githubuser.data.remote.api

import id.bobipermanasandi.githubuser.data.remote.response.FollowResponseItem
import id.bobipermanasandi.githubuser.data.remote.response.UserDetailResponse
import id.bobipermanasandi.githubuser.data.remote.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>


}