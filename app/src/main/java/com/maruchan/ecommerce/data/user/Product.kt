package com.maruchan.ecommerce.data.user


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
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
    val rating: Float?,
    @SerializedName("sizes")
    val sizes: List<Size?>?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("variants")
    val variants: List<Variant?>?,
   /* @SerializedName("cart")
    val cart: List<Cart?>?*/
    @SerializedName("product")
    val productt:Productt?
) : Parcelable {

    @Parcelize
    data class Productt(
        @Expose
        @SerializedName("active")
        val active: String?,
        @Expose
        @SerializedName("category_id")
        val categoryId: Int?,
        @Expose
        @SerializedName("created_at")
        val createdAt: String?,
        @Expose
        @SerializedName("description")
        val description: String?,
        @Expose
        @SerializedName("id")
        val id: Int?,
        @Expose
        @SerializedName("image")
        val image: String?,
        @Expose
        @SerializedName("name")
        val nameProduct: String?,
        @Expose
        @SerializedName("updated_at")
        val updatedAt: String?
    ) : Parcelable

    @Parcelize
    data class Variant(
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
        val updatedAt: String?,
        var selected: Boolean
    ) : Parcelable

    @Parcelize
    data class Size(
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("price")
        val price: String?,
        @SerializedName("size")
        val size: Int?,
        @SerializedName("stock")
        val stock: Int?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("variant_id")
        val variantId: Int?,
        var selected: Boolean
    ) : Parcelable

  /*    @Parcelize
    data class Cart(
        @SerializedName("user_id")
        val userId: Int?,
        @SerializedName("price")
        val productId: String?,
        @SerializedName("size_id")
        val sizeId: Int?,
        @SerializedName("qty")
        val qty: Int?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("variant_id")
        val variantId: Int?,
        var selected: Boolean
    ) : Parcelable*/
//    "id": 5,
//    "user_id": 1,
//    "product_id": 1,
//    "variant_id": 1,
//    "size_id": 1,
//    "qty": 24,
//    "total_cost": "2400000.00",
//    "created_at": "2022-09-26T03:35:13.000000Z",
//    "updated_at": "2022-09-26T03:35:13.000000Z"
}

