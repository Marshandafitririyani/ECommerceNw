package com.maruchan.ecommerce.data.cart


import com.google.gson.annotations.SerializedName

data class ProductCart(
    @SerializedName("active")
    val active: String?,
    @SerializedName("category_id")
    val categoryId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("updated_at")
    val updatedAt: String?
)