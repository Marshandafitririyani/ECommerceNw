package com.maruchan.ecommerce.ui.cart

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toList
import com.google.gson.Gson
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.cart.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,

    ) : BaseViewModel() {
    private val _responseSaveCart = MutableSharedFlow<List<Cart>>()
    val responseSave = _responseSaveCart.asSharedFlow()
    private val _responseAPI = MutableSharedFlow<ApiResponse>()
    val responseAPI = _responseAPI.asSharedFlow()

    fun showCart() = viewModelScope.launch {
        ApiObserver(
            { apiService.showChart() },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONArray(ApiCode.DATA).toList<Cart>(gson)
                    _responseSaveCart.emit(data)
                }
            }
        )
    }


    fun editCart(id: Int?, qty: Int?) = viewModelScope.launch {
        ApiObserver(
            { apiService.editCart(id, qty) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                }
            }
        )
    }

    fun deleteCart(id: Int) = viewModelScope.launch {
        _responseAPI.emit(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.deleteCart(id) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _responseAPI.emit(ApiResponse().responseSuccess("Delete Success"))
                }
                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _responseAPI.emit(ApiResponse().responseError())
                }
            }
        )
    }
}