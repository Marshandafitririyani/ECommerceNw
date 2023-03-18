package com.maruchan.ecommerce.ui.detail

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.data.constant.Const
import com.maruchan.ecommerce.data.user.Product
import com.maruchan.ecommerce.data.user.Product.Variant
import com.maruchan.ecommerce.databinding.ActivityDetailBinding
import com.maruchan.ecommerce.databinding.ItemColorShoesBinding
import com.maruchan.ecommerce.databinding.ItemSizeBinding
import com.maruchan.ecommerce.imageSlider.ImageSlider
import com.maruchan.ecommerce.ui.checkout.CheckoutActivity
import com.maruchan.ecommerce.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {
    private var product: Product? = null

    private val adapterColor by lazy {
        ReactiveListAdapter<ItemColorShoesBinding, Variant>(R.layout.item_color_shoes).initItem { position, data ->
        }
    }
    private val adapterSize by lazy {
        ReactiveListAdapter<ItemSizeBinding, Product.Size>(R.layout.item_size).initItem { position, data ->
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        product = intent.getParcelableExtra(Const.LIST.PRODUCK)
        binding.detail = product

        observe()
        adapter()
        initClick()

        getProduct()

    }

    private fun initClick() {
        binding.btnCheckoutDetail.setOnClickListener {
            openActivity<CheckoutActivity>()
        }

        binding.ivBackCart.setOnClickListener {
            finish()
        }
    }

    private fun adapter() {
        binding.rvColor.adapter = adapterColor
        binding.rvSize.adapter = adapterSize

    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.responseSave.collect {
                        product = it
                        adapterColor.submitList(it.variants)
                        adapterSize.submitList(it.sizes)


                    }
                }
                launch {
                    viewModel.responseSaveImage.collect {
                        initSlider(it)
                    }
                }
            }
        }
    }

    private fun getProduct() {
        product?.id?.let { viewModel.getProductById(it) }
    }

    private fun initSlider(data: List<ImageSlider>) {
        val imageList = ArrayList<SlideModel>()
        data.forEach {
            imageList.add(SlideModel(it.image))
        }
        binding.imageSliderDetail.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }
}

