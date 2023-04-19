package com.maruchan.ecommerce.ui.cart

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.helper.util.Dpx
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

    //TODO:adapter Cart
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
                //TODO:fungsi penambahan
                holder.binding.btnPluss.setOnClickListener {
                    if (qty < 100) {
                        qty++
                        item.id?.let { it1 -> editCart(it1, qty) }

                    }

                    holder.binding.btnQty.text = qty.toString()
                }

                //TODO:fungsi penguranagan
                holder.binding.btnMinus.setOnClickListener {
                    if (qty != 1) {
                        qty--
                        item.id?.let { it1 -> editCart(it1, qty) }
                    }

                    holder.binding.btnQty.text = qty.toString()
                }

                //TODO:fungsi delete cart
                holder.binding.imgDelet.setOnClickListener {
                    val builder = AlertDialog.Builder(this@CartActivity)
                    builder.setMessage("Do you want to delete the product")
                        .setCancelable(false)
                        .setPositiveButton("Delete") { dialog, id ->
                            productId?.let {
                                viewModel.deleteCart(id = it)
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClick()
        observe()
        adapter()
        showCart()

    }
    private fun btnCondition() {
        val listCart = adapterCart.currentList
        //TODO:untuk mengubah warna ketika keranjang ada product dapat di cekot dan jika keranjnag tidak ada barang tidak dapat di cekot
        if (listCart.isEmpty()) {
            binding.btnCheckoutCart.setBackgroundColor(getResources().getColor(com.denzcoskun.imageslider.R.color.grey_font))
            binding.btnCheckoutCart.setOnClickListener {
//                binding.root.snacked("Tambahkan Produk ke Keranjang Terlebih dahulu")
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
            showCart()
        }

    }

    //TODO:adapter
    private fun adapter() {
        binding.rvHome.adapter = adapterCart
        setItemTouchHelper()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    //untuk swipe refresh
                    viewModel.responseSave.collect { product ->
                        binding.swipeRefreshLayout.isRefreshing = false
                        adapterCart.submitList(product)
                        btnCondition()
                    }
                }
            }
        }

    }

    //TODO:untuk menampilkan list pada cart
    private fun showCart() {
        viewModel.showCart()
    }

    //TODO:untuk mengubah = menambah, mengurangi dan menghapus
    private fun editCart(id: Int, qty: Int) {
        viewModel.editCart(id, qty)
    }

    //komen
    //TODO:fungsi untuk delete swipe
    private fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
//            private val limitScrolX = dpTopx(100f, this@CartActivity)
            private val limitScrollX = Dpx.dpToPx(100)
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)

            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean

            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }
                    if (isCurrentlyActive) {
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initWhenInActive = dX
                        }
                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            val xInt = (currentScrollXWhenInActive * dX / initWhenInActive).toInt()
                            viewHolder.itemView.scrollTo(xInt, 0)
                        }

                    }

                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)

                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {
            attachToRecyclerView(binding.rvHome)
        }
    }

   /* private fun dpTopx(dipValue: Float, contentx: Context): Int {
        return (dipValue * contentx.resources.displayMetrics.density).toInt()
    }*/


}

