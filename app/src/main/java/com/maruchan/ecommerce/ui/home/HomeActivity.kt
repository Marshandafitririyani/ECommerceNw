package com.maruchan.ecommerce.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
import com.maruchan.ecommerce.R
import com.maruchan.ecommerce.base.activity.BaseActivity
import com.maruchan.ecommerce.data.constant.Const
import com.maruchan.ecommerce.data.user.Product
import com.maruchan.ecommerce.databinding.ActivityHomeBinding
import com.maruchan.ecommerce.databinding.ItemShoesBinding
import com.maruchan.ecommerce.ui.cart.CartActivity
import com.maruchan.ecommerce.ui.detail.DetailActivity
import com.maruchan.ecommerce.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    private val product = ArrayList<Product?>()
    private val procuctAll = ArrayList<Product?>()

    private val adapter by lazy {
        ReactiveListAdapter<ItemShoesBinding, Product>(R.layout.item_shoes).initItem { position, data ->
            val detailIntent = Intent(this, DetailActivity::class.java).apply {
                putExtra(Const.LIST.PRODUCK, data)
            }
            startActivity(detailIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()
        adapter()

        val user = session.getUser()
        if (user != null) {
            binding.home = user
        }

        binding.imgProfileHome.setOnClickListener {
            openActivity<ProfileActivity>()

        }

        binding.imgCart.setOnClickListener {
            openActivity<CartActivity>()

        }

        binding.etSearchHome.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                product.clear()
                binding.rvHome.adapter?.notifyDataSetChanged()
                product.addAll(procuctAll)
                binding.rvHome.adapter?.notifyItemInserted(0)
            } else {
                val filter = procuctAll.filter { it?.name?.contains("$text", true) == true }
                product.clear()
                filter.forEach {
                    product.add(it)
                }
                binding.rvHome.adapter?.notifyDataSetChanged()
                binding.rvHome.adapter?.notifyItemInserted(0)
            }
            if (product.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
        }
    }

    private fun adapter() {
        binding.rvHome.adapter = adapter
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    getAll()
                    viewModel.responseSave.collect { product ->
                        Log.d("data produk", "cek ${product}")
                        adapter.submitList(product)
                    }
                }
                launch {
                    viewModel.responseSave.collect {
                        product.clear()
                        procuctAll.clear()
                        adapter.submitList(product)
                        product.addAll(it)
                        procuctAll.addAll(it)
                        adapter.submitList(product)
                        if (product.isEmpty()) {
                            binding.tvEmpty.visibility = View.VISIBLE
                        } else {
                            binding.tvEmpty.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getAll() {
        viewModel.getAllProduk()
    }
}