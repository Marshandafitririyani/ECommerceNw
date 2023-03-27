package com.maruchan.ecommerce.api

import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(
        @Field("phone_number") phone: String?,
        @Field("password") password: String?
    ): String

    @FormUrlEncoded
    @POST("api/profile")
    suspend fun updateProfile(
        @Field("name") name: String?,
        @Field("phone number") phone: String?,
    ): String

    @Multipart
    @POST("api/profile")
    suspend fun updateWithPhoto(
        @Part("name") name: String,
        @Part("phone number") phone: String,
        @Part photo: MultipartBody.Part?
    ): String

    @FormUrlEncoded
    @POST("api/checkout")
    suspend fun checkout(
        @Field("address") address: String?,
        @Field("provinsi") provinsi: String?,
        @Field("kota") kota: String?,
        @Field("kecamatan") kecamatan: String?,
        @Field("note") note: String?,
    ): String

    @GET("api/product")
    suspend fun getAllProduk(
    ): String

    @GET("api/profile")
    suspend fun getProfile(): String

    @GET("api/product/{id}")
    suspend fun getProductById(
        @Path("id") id: Int?
    ): String

    @POST("api/auth/logout")
    suspend fun logout(): String

    @FormUrlEncoded
    @POST("api/chart")
    suspend fun addCart(
        @Field("size_id") sizeId: Int?,
        @Field("qty") qty: Int?
    ): String

    @GET("api/chart")
    suspend fun showChart(): String

    @POST("api/chart/edit/{id}")
    suspend fun editCart(
        @Path("qty") qty: Int?
    ): String

    @POST("api/chart/delete/{id}")
    suspend fun deletCart(): String


}