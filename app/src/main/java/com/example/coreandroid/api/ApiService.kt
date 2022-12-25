package com.example.coreandroid.api

import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("mfriend_login")
    suspend fun login(
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): String

    @FormUrlEncoded
    @POST("mfriend_register")
    suspend fun register(
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("name") name: String?
    ): String

    @GET("mfriend_list")
    suspend fun getFriends (
        @Query("id_user") id_user: Int?,
        @Query("search") search: String?,
        @Query("filter") filter: String?
    ): String

//    @GET("webs/list-article")
//    suspend fun listArticle (
//        @Query("page") page: Int?
//    ): String
}