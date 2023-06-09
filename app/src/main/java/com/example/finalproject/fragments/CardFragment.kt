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
import com.example.finalproject.models.Purchase
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

        productAdapter.setOnAddButtonClickListener {
            productViewModel.addCountCardProduct(it)
        }

        productAdapter.setOnRemoveButtonClickListener {
            productViewModel.removeCountCardProduct(it)
        }

        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("id", it)
            }
            findNavController().navigate(
                R.id.action_cardFragment_to_productDetailsFragment,
                bundle
            )
        }

        productViewModel.getCardProductList()
        binding = FragmentCardBinding.inflate(inflater,container,false)
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = productAdapter

        productViewModel.cardProductList.observe(viewLifecycleOwner, Observer {  cardProducts->
            binding.progressBar.isVisible = true
            productList.clear()
            if (cardProducts!=null) {
                productList.addAll(cardProducts)
                binding.emptyCard.isVisible = productList.isEmpty()
                binding.cardList.isVisible = productList.isNotEmpty()
                binding.totalCost.text = "Итого: ${ productList.sumOf { it.price * it.count } } тенге"
            }
            binding.progressBar.isVisible = false
            productAdapter.notifyDataSetChanged()
            //Log.d("Fragment card product list", productList.size.toString())
        })

        binding.purchaseButton.setOnClickListener {
            val purchase = Purchase(productList,productList.sumOf { it.price * it.count })
            val bundle = Bundle().apply {
                putSerializable("purchase", purchase)
            }
            findNavController()
                .navigate(R.id.action_cardFragment_to_confirmPurchaseFragment,
                bundle)
        }

        return binding.root
    }
}