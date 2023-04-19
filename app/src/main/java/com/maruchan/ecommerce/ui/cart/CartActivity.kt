package com.maruchan.ecommerce.ui.cart

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
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
                    holder.binding.btnQty.text = qty.toString()
                }
                holder.binding.btnMinus.setOnClickListener {
                    if (qty != 1) {
                        qty--
                        item.id?.let { it1 -> editCart(it1, qty) }
                    }

                    holder.binding.btnQty.text = qty.toString()
                }

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
        showCart()
        adapter()

    }

    private fun btnCondition() {
        val listCart = adapterCart.currentList
        if (listCart.isEmpty()) {
            binding.btnCheckoutCart.setBackgroundColor(ContextCompat.getColor(this@CartActivity,com.denzcoskun.imageslider.R.color.grey_font))
        } else {
            binding.btnCheckoutCart.setBackgroundColor(ContextCompat.getColor(this@CartActivity,R.color.blue_75))
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

    private fun adapter() {
        binding.rvHome.adapter = adapterCart
        setItemTouchHelper()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
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
        viewModel.showCart()
    }

    private fun editCart(id: Int, qty: Int) {
        viewModel.editCart(id, qty)
    }

    private fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private val limitScrolX = dpTopx(100f, this@CartActivity)
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
                        if (scrollOffset > limitScrolX) {
                            scrollOffset = limitScrolX
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
                        if (viewHolder.itemView.scrollX < limitScrolX) {
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
                if (viewHolder.itemView.scrollX > limitScrolX) {
                    viewHolder.itemView.scrollTo(limitScrolX, 0)

                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {
            attachToRecyclerView(binding.rvHome)
        }
    }

    private fun dpTopx(dipValue: Float, contentx: Context): Int {
        return (dipValue * contentx.resources.displayMetrics.density).toInt()
    }

}

