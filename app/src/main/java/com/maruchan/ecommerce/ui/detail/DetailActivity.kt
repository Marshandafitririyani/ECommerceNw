package com.maruchan.ecommerce.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.data.constant.Const
import com.maruchan.ecommerce.data.product.Product
import com.maruchan.ecommerce.data.product.Product.Variant
import com.maruchan.ecommerce.databinding.ActivityDetailBinding
import com.maruchan.ecommerce.databinding.ItemColorShoesBinding
import com.maruchan.ecommerce.databinding.ItemSizeBinding
import com.maruchan.ecommerce.helper.imageSlider.ImageSlider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {
    private var product: Product? = null
    private var listColor = ArrayList<Variant?>()
    private var selectColor: Variant? = null
    private var listSize = ArrayList<Product.Size?>()
    private var selectSize: Product.Size? = null

    private val adapterColor by lazy {
        object : ReactiveListAdapter<ItemColorShoesBinding, Variant>(R.layout.item_color_shoes) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemColorShoesBinding, Variant>,
                position: Int
            ) {
                listColor[position]?.let { data ->
                    holder.binding.data = data
                    holder.binding.backgroundColorShoes.setBackgroundColor(
                        if (data.selected) applicationContext.getColor(R.color.abu)
                        else applicationContext.getColor(R.color.white)
                    )
                    holder.itemView.setOnClickListener {
                        listColor.forEachIndexed { index, variant ->
                            variant?.selected = index == position
                        }
                        notifyDataSetChanged()
                        selectColor = data
                        condititonForColor(data.id)
                    }
                }
            }
        }.initItem()
    }
    private val adapterSize by lazy {
        object : ReactiveListAdapter<ItemSizeBinding, Product.Size>(R.layout.item_size) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemSizeBinding, Product.Size>,
                position: Int
            ) {
                listSize[position]?.let { data ->
                    holder.binding.data = data
                    holder.binding.backgroundColorSize.setBackgroundColor(
                        if (data.selected) applicationContext.getColor(R.color.abu)
                        else applicationContext.getColor(R.color.white)
                    )
                    holder.itemView.setOnClickListener {
                        listSize.forEachIndexed { index, Size ->
                            Size?.selected = index == position
                        }
                        notifyDataSetChanged()
                        selectSize = data
                        if (data.selected) {
                            binding.btnCheckoutDetail.setBackgroundColor(getResources().getColor(R.color.blue_75))
                        } else {
                            binding.btnCheckoutDetail.setBackgroundColor(getResources().getColor(R.color.abu))

                        }
                    }
                }
            }

        }.initItem()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        product = intent.getParcelableExtra(Const.LIST.PRODUCT)
        binding.detail = product

        observe()
        adapter()
        initClick()

        getProduct()

    }

    private fun condititonForColor(idVarian: Int?) {
        if (selectColor == null) {
            binding.rvSize.visibility = View.INVISIBLE
        } else {
            binding.rvSize.visibility = View.VISIBLE
            val filterSize = listSize.filter {
                it?.variantId == idVarian
            }
            adapterSize.submitList(filterSize)

        }
    }

    private fun initClick() {
        binding.ivBackCart.setOnClickListener {
            finish()
        }
        binding.btnCheckoutDetail.setOnClickListener {
            addCart()
        }


    }

    private fun adapter() {
        binding.rvColor.adapter = adapterColor
        binding.rvSize.visibility = View.INVISIBLE
        binding.rvSize.adapter = adapterSize

    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.responseAPI.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> {
                                loadingDialog.show("Loading")
                            }
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                finish()
                            }
                            else -> {
                                loadingDialog.setResponse(it.message ?: return@collect)
                            }
                        }
                    }
                }
                launch {
                    viewModel.responseSave.collect {
                        product = it
                        adapterColor.submitList(it.variants)

                        listColor.clear()
                        it.variants?.let { list ->
                            listColor.addAll(list)
                        }
                        listSize.clear()
                        it.sizes?.let { list ->
                            listSize.addAll(list)

                        }
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

    private fun addCart() {
        Log.d("cekSelectedAdd", "cekSelectedAdd : ${selectSize?.selected}")
        if (selectSize?.selected == true) {
            selectSize?.let {
                selectSize?.id?.let { viewModel.addCart(sizeId = it, qty = 1) }
            }
        }
    }

    private fun initSlider(data: List<ImageSlider>) {
        val imageList = ArrayList<SlideModel>()
        data.forEach {
            imageList.add(SlideModel(it.image))
        }
        binding.imageSliderDetail.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }
}


