package com.maruchan.ecommerce.ui.edit

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toObject
import com.google.gson.Gson
import com.maruchan.ecommerce.api.ApiService
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import com.maruchan.ecommerce.data.session.Session
import com.maruchan.ecommerce.data.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson,

    ) : BaseViewModel() {
    fun updateProfile(name: String, phoneNumber: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver({
            apiService.updateProfile(name, phoneNumber)
        }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                _apiResponse.send(ApiResponse().responseSuccess("Profile Updated"))
                session.saveUser(data)
                if (status == ApiCode.SUCCESS) {

                }
            }
            override suspend fun onError(response: ApiResponse) {
                super.onError(response)
                _apiResponse.send(ApiResponse().responseError())
            }
        })
    }

    fun updateWithPhoto(name: String, phoneNumber: String, photo: File) = viewModelScope.launch {
        val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("image", photo.name, fileBody)

        ApiObserver(
            { apiService.updateWithPhoto(name, phoneNumber, filePart) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {

                    val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                    _apiResponse.send(ApiResponse().responseSuccess("profile updated"))
                    session.saveUser(data)
                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _apiResponse.send(ApiResponse().responseError())
                }


            }
        )
    }
}