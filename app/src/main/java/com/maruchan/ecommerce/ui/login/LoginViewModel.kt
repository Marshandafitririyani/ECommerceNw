package com.maruchan.ecommerce.ui.login

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.google.gson.Gson
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.constant.Const
import com.maruchan.ecommerce.data.session.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session

) : BaseViewModel() {
    fun login(phone: String, password: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.login(phone, password) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val status = response.getInt(ApiCode.STATUS)
                    val newToken = response.getString("token")
                    session.setValue(Const.TOKEN.API_TOKEN, newToken)
                    _apiResponse.send(ApiResponse().responseSuccess())
                }
                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _apiResponse.send(ApiResponse().responseError())
                }
            })
    }
}
