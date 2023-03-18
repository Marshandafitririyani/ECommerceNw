package com.maruchan.ecommerce.ui.splash

import androidx.lifecycle.viewModelScope
import com.maruchan.ecommerce.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {
    fun splash(done: () -> Unit) = viewModelScope.launch {
        delay(1000)
        done()
    }
}