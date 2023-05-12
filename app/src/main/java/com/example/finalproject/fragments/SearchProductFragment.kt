package com.example.finalproject.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchProductFragment : BaseFragment() {

    lateinit var binding: FragmentSearchProductBinding
    lateinit var productList: ArrayList<Product>
    lateinit var filteringList: ArrayList<Product>
    lateinit var productAdapter: ProductAdapter

    val productViewModel by viewModels<ProductViewModel>()

    var startPrice = 500F
    var endPrice = 10000F
    var startRating = 0
    var endRating = 5
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productList = ArrayList()
        filteringList = ArrayList()
        productAdapter = ProductAdapter(filteringList)

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
            if (it!=null) {
                productList.clear()
                filteringList.clear()
                productList.addAll(it)
                filteringList.addAll(productList)
                Log.i("filteringList", filteringList.size.toString())
                productAdapter.notifyDataSetChanged()
                search()
                filter()
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
                    filterProductList(startPrice,endPrice, startRating,endRating)
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

    private fun filter(){
        filterProductList(startPrice,endPrice,startRating, endRating)

        binding.rangeSliderPrice.addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = binding.rangeSliderPrice.values
                startPrice = values[0]
                endPrice = values[1]
                Log.i("SliderPreviousValue From", startPrice.toString())
                Log.i("SliderPreviousValue To", endPrice.toString())
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = binding.rangeSliderPrice.values
                startPrice = values[0]
                endPrice = values[1]
                Log.i("SliderNewValue From", startPrice.toString())
                Log.i("SliderNewValue To", endPrice.toString())
                filterProductList(startPrice,endPrice,startRating, endRating)
            }
        })

        binding.rangeSliderRating.addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener{
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    val values = binding.rangeSliderRating.values
                    startRating = values[0].toInt()
                    endRating = values[1].toInt()
                    Log.i("SliderPreviousValue From", startRating.toString())
                    Log.i("SliderPreviousValue To", endRating.toString())
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    val values = binding.rangeSliderRating.values
                    startRating = values[0].toInt()
                    endRating = values[1].toInt()
                    Log.i("SliderNewValue From", startRating.toString())
                    Log.i("SliderNewValue To", endRating.toString())
                    filterProductList (startPrice,endPrice,startRating, endRating)
                }
            })


    }
    private fun filterProductList(startPrice: Float, endPrice: Float, startRating: Int, endRating: Int){
        val toFilter = productList
        val filtering = toFilter.filter {
                product -> product.price >= startPrice && endPrice >= product.price &&
                product.rating >= startRating && product.rating <= endRating
        }
        Log.i("filtering", filtering.size.toString())
        filteringList.clear()
        filteringList.addAll(filtering)
        Log.i("filteringList", filteringList.size.toString())
        Log.i("productList", productList.size.toString())

        if (binding.searchTitle.text.isNotEmpty())
            productAdapter.filter.filter(binding.searchTitle.text)

        productAdapter.notifyDataSetChanged()
    }

}