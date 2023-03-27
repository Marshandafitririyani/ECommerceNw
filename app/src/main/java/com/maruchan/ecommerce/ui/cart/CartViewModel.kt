package com.maruchan.ecommerce.ui.cart

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.extension.toList
import com.crocodic.core.extension.toObject
import com.google.gson.Gson
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.session.Session
import com.maruchan.ecommerce.data.user.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session

) : BaseViewModel() {
    private val _responseSaveCart = MutableSharedFlow<List<Product>>()
    val responseSave = _responseSaveCart.asSharedFlow()
    /*private val _responseSaveCartProduct = MutableSharedFlow<List<Product.Productt>>()
    val responseSaveProduct = _responseSaveCartProduct.asSharedFlow()*/


    fun showChart() = viewModelScope.launch {
        ApiObserver(
            { apiService.showChart()},
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONArray(ApiCode.DATA).toList<Product>(gson)
                    _responseSaveCart.emit(data)
                    Timber.d("cek api ${data.size}")
                }
            }
        )
    }
/*
   fun showChart() = viewModelScope.launch {
       ApiObserver(
           { apiService.showChart()},
           false,
           object : ApiObserver.ResponseListener {
               override suspend fun onSuccess(response: JSONObject) {
                   val data = response.getJSONArray(ApiCode.DATA).toList<Product>(gson)
                   _responseSaveCart.emit(data)
                   Timber.d("cek api ${data.size}")
               }
           }
       )
   }
*/

   /* fun editCart(qty: Int) = viewModelScope.launch {
        ApiObserver(
            { apiService.editCart(qty = 1 )},
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONArray(ApiCode.DATA).toList<Product>(gson)
                    _responseSaveCart.emit(data)
                    Timber.d("cek api ${data.size}")
                }
            }
        )
    }
*/
}