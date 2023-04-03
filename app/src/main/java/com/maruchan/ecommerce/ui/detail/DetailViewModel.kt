package com.maruchan.ecommerce.ui.detail

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toList
import com.crocodic.core.extension.toObject
import com.google.gson.Gson
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.session.Session
import com.maruchan.ecommerce.data.product.Product
import com.maruchan.ecommerce.helper.imageSlider.ImageSlider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson,

    ) : BaseViewModel() {
    private val _responseSave = MutableSharedFlow<Product>()
    val responseSave = _responseSave.asSharedFlow()
    private val _responseSaveImage = MutableSharedFlow<List<ImageSlider>>()
    val responseSaveImage = _responseSaveImage.asSharedFlow()
    private val _responseAPI = MutableSharedFlow<ApiResponse>()
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

    fun addCart(sizeId: Int, qty: Int = 1) = viewModelScope.launch {
        _responseAPI.emit(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.addCart(sizeId,qty) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _responseAPI.emit(ApiResponse().responseSuccess("Add Cart Success"))
                }
                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _responseAPI.emit(ApiResponse().responseError())
                }
            }
        )
    }

}


