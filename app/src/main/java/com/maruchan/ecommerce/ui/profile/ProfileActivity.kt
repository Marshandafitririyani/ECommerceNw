package com.maruchan.ecommerce.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.openActivity
import com.crocodic.core.helper.ImagePreviewHelper
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.databinding.ActivityProfileBinding
import com.maruchan.ecommerce.ui.edit.EditProfileActivity
import com.maruchan.ecommerce.ui.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity :
    BaseActivity<ActivityProfileBinding, ProfileViewModel>(R.layout.activity_profile) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = session.getUser()
        if (user != null) {
            binding.user = user
        }


        binding.ivImageProfile.setOnClickListener {
            ImagePreviewHelper(this).show(binding.ivImageProfile, user?.image)
        }
        binding.ivEditProfile.setOnClickListener {
            openActivity<EditProfileActivity>()
        }

        binding.ivLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.ivEditProfile.setOnClickListener {
            val kembali = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("photoFile", binding?.user?.image)
                putExtra("username", binding?.user?.name)
                putExtra("phoneNumber", binding?.user?.phoneNumber)
            }
            startActivity(kembali)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> {
                                loadingDialog.show()
                            }
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<SplashActivity> {
                                    finishAffinity()

                                }
                            }
                            ApiStatus.ERROR -> {
                                loadingDialog.setResponse(it.message ?: return@collect)
                            }
                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }

                }
            }
        }
    }
}
