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

    @GET("webs/list-article")
    suspend fun listArticle (
        @Query("page") page: Int?
    ): String
}