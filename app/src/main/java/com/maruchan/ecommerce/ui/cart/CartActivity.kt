package com.maruchan.ecommerce.ui.cart

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
import com.crocodic.core.helper.ImagePreviewHelper
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.data.cart.Cart
import com.maruchan.ecommerce.data.product.Product
import com.maruchan.ecommerce.databinding.ActivityCartBinding
import com.maruchan.ecommerce.databinding.ItemCartBinding
import com.maruchan.ecommerce.ui.checkout.CheckoutActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>(R.layout.activity_cart) {

    private var product: Product? = null
    private var cart: Cart? = null
    private val productt = ArrayList<Cart?>()
    private val producttAll = ArrayList<Cart?>()

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
                holder.binding.btnPluss.setOnClickListener {
                    if (qty < 100) {
                        qty++
                        item.id?.let { it1 -> editCart(it1, qty) }

                    }
//                    notifyItemChanged(position
                    holder.binding.btnQty.text = qty.toString()
                }

                holder.binding.btnMinus.setOnClickListener {
                    if (qty != 1) {
                        qty--
                        item.id?.let { it1 -> editCart(it1, qty) }
                    }
//                    notifyItemChanged(position)
                    holder.binding.btnQty.text = qty.toString()
                }

                holder.binding.imgDelet.setOnClickListener {
                    productId?.let {
                        viewModel.deleteCart(id = it)
                    }

                }

            }
//            val detailIntent = Intent(this, CartActivity::class.java).apply {
//                putExtra(Const.LIST.PRODUCK, data)
//            }
//            startActivity(detailIntent)
        }
    }

    /* private val adapterCart by lazy {
         ReactiveListAdapter<ItemCartBinding, Product>(R.layout.item_cart).initItem { position, data ->
             val detailIntent = Intent(this, CartActivity::class.java).apply {
                 putExtra(Const.LIST.PRODUCK, data)
             }
             startActivity(detailIntent)
         }
     }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClick()
        observe()
        adapter()

    }

    private fun initClick() {
        binding.ivBackCart.setOnClickListener {
            finish()
        }
        binding.btnCheckoutCart.setOnClickListener {
            openActivity<CheckoutActivity>()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            observe()
//            product?.id?.let { viewModel.showChart() }
        }

    }

    private fun adapter() {
        binding.rvHome.adapter = adapterCart
    }

    /*private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    getCart()
//                    viewModel.responseSave.collect { product ->
//                        Log.d("data produk", "cek ${product}")
//                        adapterCart.submitList(product)
//                    }
                    viewModel.responseSaveProduct.collect{ product ->
                        adapterCart.submitList(product)
                    }
                }
            }
        }
    }*/
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
                    }
                }
                /*viewModel.responseSave.collect {
                 binding.swipeRefreshLayout.isRefreshing = false
                        productt.clear()
                    producttAll.addAll(it)
                    binding?.rvHome?.adapter?.notifyDataSetChanged()

                    }*/

            }
        }

    }

    private fun showCart() {
        viewModel.showChart()
    }

    private fun editCart(id: Int, qty: Int) {
        viewModel.editCart(id, qty)
    }

    /*private fun deleteCart(){
        viewModel.deleteCart()
    }*/
}


  /* private fun buttonClicked(btn: Button) {
     if (isOperatorClicked) {
         operand1 = strNumber.toString().toInt()
         strNumber.clear()
         isOperatorClicked = false
     }
     strNumber.append(btn.text)
     nothingTV.text = strNumbe

enum class Operator {MUL, DIV, ADD, SUB, NONE}
*/

