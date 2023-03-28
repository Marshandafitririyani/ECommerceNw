package com.maruchan.ecommerce.data.cart


import com.google.gson.annotations.SerializedName

data class VariantCart(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("updated_at")
    val updatedAt: String?
)