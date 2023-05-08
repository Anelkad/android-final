package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.adapters.CardProductAdapter
import com.example.finalproject.databinding.FragmentCardBinding
import com.example.finalproject.models.CardProduct
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardFragment : BaseFragment() {

    lateinit var binding: FragmentCardBinding
    lateinit var productList: ArrayList<CardProduct>
    lateinit var productAdapter: CardProductAdapter
    val productViewModel by viewModels<ProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        productList = ArrayList()
        productAdapter = CardProductAdapter(productList)

        productViewModel.getCardProductList()
        binding = FragmentCardBinding.inflate(inflater,container,false)
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = productAdapter

        productViewModel.cardProductList.observe(viewLifecycleOwner, Observer {
            productList.clear()
            if (!it.isNullOrEmpty()) productList.addAll(it)
            binding.progressBar.isVisible = productList.size == 0
            productAdapter.notifyDataSetChanged()
            //Log.d("Fragment card product list", productList.size.toString())
        })

        binding.purchaseButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_cardFragment_to_confirmPurchaseFragment)
        }

        return binding.root
    }
}