package com.maruchan.ecommerce.data.product


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
    @SerializedName("product")
    val productt: Productt?
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
        @SerializedName("totalCost")
        val totalCost: Int?,
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
}

