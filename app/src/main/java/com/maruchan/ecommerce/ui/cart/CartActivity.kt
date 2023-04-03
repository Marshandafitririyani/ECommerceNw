package com.maruchan.ecommerce.ui.cart

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.snacked
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.data.cart.Cart
import com.maruchan.ecommerce.databinding.ActivityCartBinding
import com.maruchan.ecommerce.databinding.ItemCartBinding
import com.maruchan.ecommerce.ui.checkout.CheckoutActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>(R.layout.activity_cart) {

    /*    private var product: Product? = null
     private var cart: Cart? = null
     private val productt = ArrayList<Cart?>()
     private val producttAll = ArrayList<Cart?>()*/

    //adapter Cart
    private val adapterCart by lazy {
        object : ReactiveListAdapter<ItemCartBinding, Cart>(R.layout.item_cart) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemCartBinding, Cart>,
                position: Int
            ) {
                val item = getItem(position)
                var qty = item.qty ?: 1
                val productId = item.id

                holder.binding.data = item
                holder.binding.btnQty.text = qty.toString()
                //fungsi penambahan
                holder.binding.btnPluss.setOnClickListener {
                    if (qty < 100) {
                        qty++
                        item.id?.let { it1 -> editCart(it1, qty) }

                    }

                    holder.binding.btnQty.text = qty.toString()
                }

                //fungsi penguranagan
                holder.binding.btnMinus.setOnClickListener {
                    if (qty != 1) {
                        qty--
                        item.id?.let { it1 -> editCart(it1, qty) }
                    }

                    holder.binding.btnQty.text = qty.toString()
                }

                holder.binding.imgDelet.setOnClickListener {
                    productId?.let {
                        viewModel.deleteCart(id = it)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClick()
        observe()
        adapter()

    }
    private fun btnCondition() {
        val listCart = adapterCart.currentList
        //untuk mengubah warna ketia keranjang ada dapat di cekot dan jika keranjnag tidak ada barang tidak dapata di cekot
        if (listCart.isEmpty()) {
            Log.d("adapter", "cek $listCart")
            binding.btnCheckoutCart.setBackgroundColor(getResources().getColor(com.denzcoskun.imageslider.R.color.grey_font))
            binding.btnCheckoutCart.setOnClickListener {
                binding.root.snacked("Tambahkan Produk ke Keranjang Terlebih dahulu")
            }
        } else {
            binding.btnCheckoutCart.setBackgroundColor(getResources().getColor(R.color.blue_75))
            binding.btnCheckoutCart.setOnClickListener {
                openActivity<CheckoutActivity>()
                finish()
            }
        }
    }

    private fun initClick() {
        binding.ivBackCart.setOnClickListener {
            finish()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            observe()
        }

    }

    private fun adapter() {
        binding.rvHome.adapter = adapterCart
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    showCart()
//                    viewModel.responseSave.collect { product ->
//                        Log.d("data produk", "cek ${product}")
//                        adapterCart.submitList(product)
//                    }
                    viewModel.responseSave.collect { product ->
                        binding.swipeRefreshLayout.isRefreshing = false
                        adapterCart.submitList(product)
                        btnCondition()
                    }
                }
            }
        }

    }

    private fun showCart() {
        viewModel.showChart()
    }

    private fun editCart(id: Int, qty: Int) {
        viewModel.editCart(id, qty)
    }

}

