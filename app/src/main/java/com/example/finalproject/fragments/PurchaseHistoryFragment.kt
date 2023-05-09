package com.example.finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.adapters.ProductAdapter
import com.example.finalproject.adapters.PurchaseHistoryAdapter
import com.example.finalproject.databinding.FragmentPurchaseHistoryBinding
import com.example.finalproject.models.Purchase
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment() {

    lateinit var binding: FragmentPurchaseHistoryBinding
    lateinit var purchaseList: ArrayList<Purchase>
    lateinit var purchasesAdapter: PurchaseHistoryAdapter

    val productViewModel by viewModels<ProductViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        purchaseList = ArrayList()
        productViewModel.getPurchaseList()

        purchasesAdapter = PurchaseHistoryAdapter(purchaseList)
        binding = FragmentPurchaseHistoryBinding.inflate(inflater,container,false)

        binding.listView.adapter = purchasesAdapter

        productViewModel.purchaseList.observe(viewLifecycleOwner, Observer {
            purchaseList.clear()
            if (it!=null) purchaseList.addAll(it)
            binding.progressBar.isVisible = purchaseList.isEmpty()
            purchasesAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

}