package com.maruchan.ecommerce.ui.splash

import android.os.Bundle
import com.crocodic.core.extension.openActivity
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.databinding.ActivitySplashBinding
import com.maruchan.ecommerce.ui.home.HomeActivity
import com.maruchan.ecommerce.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userLogin = session.getUser()

        viewModel.splash {
            if (userLogin == null) {
                openActivity<LoginActivity>()
            } else {
                openActivity<HomeActivity>()
            }
            finish()
        }
    }
}