package com.maruchan.ecommerce.ui.checkout

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.databinding.ActivityCheckoutBinding
import com.maruchan.ecommerce.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutActivity :
    BaseActivity<ActivityCheckoutBinding, CheckoutViewModel>(R.layout.activity_checkout) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnCheckoutCheckout.setOnClickListener {
            if (binding.etAlamatCheckout.isEmptyRequired(R.string.label_must_fill) ||
                binding.etProvinsiCheckout.isEmptyRequired(R.string.label_must_fill) ||
                binding.etKotaCheckout.isEmptyRequired(R.string.label_must_fill) ||
                binding.etKecamatanCheckout.isEmptyRequired(R.string.label_must_fill) ||
                binding.etNoteCheckout.isEmptyRequired(R.string.label_must_fill)
            ) {
                return@setOnClickListener
            }
            val address = binding.etAlamatCheckout.textOf()
            val provinsi = binding.etProvinsiCheckout.textOf()
            val kota = binding.etKotaCheckout.textOf()
            val kecamatan = binding.etKecamatanCheckout.textOf()
            val note = binding.etNoteCheckout.textOf()

            viewModel.checkout(address, provinsi, kota, kecamatan, note)
        }

        binding.ivBackCheckout.setOnClickListener {
           finish()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("Please Wait Checkout")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<HomeActivity>()
                                finish()

                            }
                            ApiStatus.ERROR -> {
                                disconnect(it)
                                loadingDialog.dismiss()
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
