package com.example.finalproject.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.finalproject.R
import com.example.finalproject.adapters.ProductAdapter
import com.example.finalproject.databinding.FragmentSearchProductBinding
import com.example.finalproject.models.Product
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchProductFragment : BaseFragment() {

    lateinit var binding: FragmentSearchProductBinding
    lateinit var productList: ArrayList<Product>
    lateinit var productAdapter: ProductAdapter

    val productViewModel by viewModels<ProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productList = ArrayList()
        productAdapter = ProductAdapter(productList)

        binding = FragmentSearchProductBinding.inflate(inflater,container,false)
        binding.listView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.listView.adapter = productAdapter

        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("id", it)
            }
            findNavController().navigate(
                R.id.action_searchProductFragment_to_productDetailsFragment,
                bundle
            )
        }
        productAdapter.setOnButtonClickListener { product ->
            productViewModel.addProductToCard(product)
            productViewModel.addProductToCardState.observe(viewLifecycleOwner, Observer{
                when(it){
                    is Resource.Failure -> {
                        //hideWaitDialog()
                        showSnackBar("Can't add product!",true)
                    }
                    is Resource.Loading -> {
                        //showWaitDialog()
                    }
                    is Resource.Success -> {
                        //hideWaitDialog()
                        showSnackBar("Product ${it.getSuccessResult().title} added to card!",false)
                    }
                    else -> Unit
                }
            })
        }

        loadProducts()

        return binding.root
    }

    private fun loadProducts(){
        productViewModel.getProductList()
        productViewModel.productList.observe(viewLifecycleOwner, Observer {
            productList.clear()
            if (it!=null) {
                productList.addAll(it)
                productAdapter.notifyDataSetChanged()
                search()
                if (binding.searchTitle.text.isNotEmpty())
                    productAdapter.filter.filter(binding.searchTitle.text)
            }
            binding.progressBar.isVisible = productList.isEmpty()

        })
    }

    private fun search(){
        binding.searchTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    productAdapter.filter.filter(s)
                    //Log.d("on text",s.toString())
                }
                catch (e:Exception){
                    //Log.d("text change","onTextChange: ${e.message}")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}