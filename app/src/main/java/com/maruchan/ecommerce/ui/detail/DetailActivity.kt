package com.maruchan.ecommerce.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.snacked
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
import timber.log.Timber


@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {
    private var product: Product? = null
    private var listColor = ArrayList<Variant?>()
    private var selectColor: Variant? = null
    private var listSize = ArrayList<Product.Size?>()
    private var selectSize: Product.Size? = null

    //TODO:adapter color
    private val adapterColor by lazy {
        object : ReactiveListAdapter<ItemColorShoesBinding, Variant>(R.layout.item_color_shoes) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemColorShoesBinding, Variant>,
                position: Int
            ) {
                listColor[position]?.let { data ->
                    //TODO:menampilkaan view
                    holder.binding.data = data
                    holder.binding.backgroundColorShoes.setBackgroundColor(
                        if (data.selected) applicationContext.getColor(R.color.abu)
                        else applicationContext.getColor(R.color.white)
                    )
                    //TODO:menampilkan data
                    holder.itemView.setOnClickListener {
                        listColor.forEachIndexed { index, variant ->
                            variant?.selected = index == position
                        }
                        notifyDataSetChanged()
                        selectColor = data
                        condititonForColor(data.id)
                        Timber.d("CekListColors: $listColor")
                        println("CekListColors: $listColor")
                    }
                }
            }
        }.initItem()
    }
    //TODO:adapter size
    private val adapterSize by lazy {
        object : ReactiveListAdapter<ItemSizeBinding, Product.Size>(R.layout.item_size) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemSizeBinding, Product.Size>,
                position: Int
            ) {
                listSize[position]?.let { data ->
                    //TODO:menampilkaan view
                    holder.binding.data = data
                    holder.binding.backgroundColorSize.setBackgroundColor(
                        if (data.selected) applicationContext.getColor(R.color.abu)
                        else applicationContext.getColor(R.color.white)
                    )
                    //TODO:menampilkaan data
                    holder.itemView.setOnClickListener {
                        listSize.forEachIndexed { index, Size ->
                            Size?.selected = index == position
                        }
                        notifyDataSetChanged()
                        selectSize = data
                        Timber.d("CekListColors: $listSize")
                        println("CekListColors: $listSize")
                        //TODO:untuk mengubah warna saat ingin di add cart
                        if(data.selected) {
                            binding.btnCheckoutDetail.setBackgroundColor(getResources().getColor(R.color.blue_75))
                        }else{
                            binding.btnCheckoutDetail.setBackgroundColor(getResources().getColor(R.color.abu))

                        }
                    }

                }
            }

        }.initItem()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO:getParcelable
        product = intent.getParcelableExtra(Const.LIST.PRODUCK)
        binding.detail = product

        observe()
        adapter()
        initClick()
        getProduct()

    }

    //TODO:kondisi untuk warnanya(color)
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

    //TODO:adapter color dan size
    private fun adapter() {
        binding.rvColor.adapter = adapterColor
        binding.rvSize.visibility = View.INVISIBLE
        binding.rvSize.adapter = adapterSize

    }

    //TODO:kondisi loading
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
                //TODO:untuk submit varian, color dan size
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
                //TODO:untuk image slidernya
                launch {
                    viewModel.responseSaveImage.collect {
                        initSlider(it)
                    }
                }

            }
        }
    }

    //TODO:untuk productnya
    private fun getProduct() {
        product?.id?.let { viewModel.getProductById(it) }
    }

    //TODO:untuk kondisi saat menambahkan barang ke cart dan buttom berubah warna dan jika belum memilih dia akan mengeluarkan pesan
    private fun addCart() {
        if (selectSize?.selected == true) {
            selectSize?.let {
                selectSize?.id?.let { viewModel.addCart(sizeId = it, qty = 1) }
            }
        }
        }


    //TODO:image slider
    private fun initSlider(data: List<ImageSlider>) {
        val imageList = ArrayList<SlideModel>()
        data.forEach {
            imageList.add(SlideModel(it.image))
        }
        binding.imageSliderDetail.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }
}


