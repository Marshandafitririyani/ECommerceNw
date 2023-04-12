package com.maruchan.ecommerce.ui.checkout

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val apiService: ApiService,

    ) : BaseViewModel() {

    private val _responseSave = MutableSharedFlow<List<Product>>()
    val responseSave = _responseSave.asSharedFlow()

    fun checkout(
        alamat: String?,
        provinsi: String?,
        kota: String?,
        kecamatan: String?,
        catatan: String?
    ) = viewModelScope.launch {
        ApiObserver(
            { apiService.checkout(alamat, provinsi, kota, kecamatan, catatan) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _apiResponse.send(ApiResponse().responseSuccess("Checkout Succes"))
                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _apiResponse.send(ApiResponse(message = "Error").responseError())
                }
            }
        )
    }
}