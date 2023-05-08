package com.example.finalproject.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.models.Product
import com.example.finalproject.repositories.ProductRepositoryImp
import com.example.finalproject.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepositoryImp
): ViewModel() {

    private val _addProductState = MutableLiveData<Resource<Product>?>(null)
    val addProductState: LiveData<Resource<Product>?> = _addProductState

    private val _productList = MutableLiveData<ArrayList<Product>>(null)
    val productList: LiveData<ArrayList<Product>> = _productList

    private val _productDetailState = MutableLiveData<Resource<Product>?>(null)
    val productDetailState: LiveData<Resource<Product>?> = _productDetailState

    fun addProduct(product: Product) = viewModelScope.launch {
        _addProductState.value = Resource.Loading
        val result = repository.addProduct(product)
        _addProductState.value = result
    }

    fun getProductDetails(id: String) = viewModelScope.launch {
        _productDetailState.value = Resource.Loading
        val result = repository.getProductDetails(id)
        _productDetailState.value = result
        Log.d("product view model", result.toString())
    }

    fun getProductList() = viewModelScope.launch {
            repository.getProductList().collect {
                when (it) {
                    is Resource.Success -> {
                        _productList.value = it.getSuccessResult()
                    }
                    is Resource.Failure -> {
                        Log.d("product view model", "error")
                    }
                    else -> Unit
                }
            }
        }
}