package com.maruchan.ecommerce.imageSlider

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageSlider (
    @Expose
    @SerializedName("image")
    val image: String?
)