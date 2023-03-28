package com.maruchan.ecommerce.data.cart


import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("product")
    val product: ProductCart?,
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("qty")
    val qty: Int?,
    @SerializedName("size_id")
    val sizeId: Int?,
    @SerializedName("total_cost")
    val totalCost: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("variant")
    val variant: VariantCart?,
    @SerializedName("variant_id")
    val variantId: Int?
)