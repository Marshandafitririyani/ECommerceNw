package com.maruchan.ecommerce.helper.imageSlider

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageSlider (
    @Expose
    @SerializedName("image")
    val image: String?
)