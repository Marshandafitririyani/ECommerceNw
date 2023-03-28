package com.maruchan.ecommerce.ui.detail

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toList
import com.crocodic.core.extension.toObject
import com.google.gson.Gson
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.session.Session
import com.maruchan.ecommerce.data.user.Product
import com.maruchan.ecommerce.imageSlider.ImageSlider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson,

    ) : BaseViewModel() {
    private val _responseSave = MutableSharedFlow<Product>()
    private val _responseSaveImage = MutableSharedFlow<List<ImageSlider>>()
    private val _responseAPI = MutableSharedFlow<ApiResponse>()
    val responseSave = _responseSave.asSharedFlow()
    val responseSaveImage = _responseSaveImage.asSharedFlow()
    val responseAPI = _responseAPI.asSharedFlow()


    fun getProductById(id: Int) = viewModelScope.launch {
        ApiObserver(
            { apiService.getProductById(id) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val imageSlider =
                        response.getJSONArray("image_sliders").toList<ImageSlider>(gson)
                    _responseSaveImage.emit(imageSlider)
                    val data = response.getJSONObject("data").toObject<Product>(gson)
                    _responseSave.emit(data)
                }
            }
        )
    }

    fun addCaert(sizeId: Int, qty: Int = 1) = viewModelScope.launch {
        _responseAPI.emit(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.addCart(sizeId,qty) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val status = response.getInt(ApiCode.STATUS)
                    val data = response.getJSONObject(ApiCode.DATA).toObject<Product>(gson)
                    _responseAPI.emit(ApiResponse().responseSuccess())
                }
                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _responseAPI.emit(ApiResponse().responseError())
                }
            }
        )
    }
}


