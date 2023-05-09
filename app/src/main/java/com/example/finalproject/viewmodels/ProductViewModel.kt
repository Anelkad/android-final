package com.example.finalproject.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Product
import com.example.finalproject.models.Purchase
import com.example.finalproject.repositories.ProductRepositoryImp
import com.example.finalproject.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.security.CryptoPrimitive
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepositoryImp
): ViewModel() {

    private val _addProductState = MutableLiveData<Resource<Product>?>(null)
    val addProductState: LiveData<Resource<Product>?> = _addProductState

    private val _addProductToCardState = MutableLiveData<Resource<CardProduct>?>(null)
    val addProductToCardState: LiveData<Resource<CardProduct>?> = _addProductToCardState

    private val _productList = MutableLiveData<ArrayList<Product>>(null)
    val productList: LiveData<ArrayList<Product>> = _productList

    private val _cardProductList = MutableLiveData<ArrayList<CardProduct>>(null)
    val cardProductList: LiveData<ArrayList<CardProduct>> = _cardProductList

    private val _purchaseList = MutableLiveData<ArrayList<Purchase>>(null)
    val purchaseList: LiveData<ArrayList<Purchase>> = _purchaseList


    private val _productDetail = MutableLiveData<Product>(null)
    val productDetail: LiveData<Product> = _productDetail

    private val _purchaseState = MutableLiveData<Resource<Purchase>?>(null)
    val purchaseState: LiveData<Resource<Purchase>?> = _purchaseState

    fun addProduct(product: Product) = viewModelScope.launch {
        _addProductState.value = Resource.Loading
        val result = repository.addProduct(product)
        _addProductState.value = result
    }

    fun addProductToCard(product: CardProduct) = viewModelScope.launch {
        _addProductToCardState.value = Resource.Loading
        val result = repository.addProductToCard(product)
        _addProductToCardState.value = result
    }

    fun purchase(purchase: Purchase) = viewModelScope.launch {
        _purchaseState.value = Resource.Loading
        val result = repository.purchase(purchase)
        _purchaseState.value = result
    }

    fun addCountCardProduct(id: String) = viewModelScope.launch {
        repository.addCountCardProduct(id)
        //Log.d("product view model", result.toString())
    }

    fun removeCountCardProduct(id: String) = viewModelScope.launch {
        repository.removeCountCardProduct(id)
        //Log.d("product view model", result.toString())
    }

    fun clearCard() = viewModelScope.launch {
        repository.clearCard()
    }

    fun getProductDetails(id: String) = viewModelScope.launch {
        repository.getProductDetails(id).collect{
            when (it) {
                is Resource.Success -> {
                    _productDetail.value = it.getSuccessResult()
                }
                is Resource.Failure -> {
                    Log.d("product detail view model", "error")
                }
                else -> Unit
            }
        }
    }

    fun getProductList() = viewModelScope.launch {
            repository.getProductList().collect {
                when (it) {
                    is Resource.Success -> {
                        _productList.value = it.getSuccessResult()
                    }
                    is Resource.Failure -> {
                        Log.d("product list view model", "error")
                    }
                    else -> Unit
                }
            }
        }

    fun getPurchaseList() = viewModelScope.launch {
        repository.getPurchaseList().collect {
            when (it) {
                is Resource.Success -> {
                    _purchaseList.value = it.getSuccessResult()
                }
                is Resource.Failure -> {
                    Log.d("product list view model", "error")
                }
                else -> Unit
            }
        }
    }

    fun getCardProductList() = viewModelScope.launch {
        repository.getCardProductList().collect {
            when (it) {
                is Resource.Success -> {
                    _cardProductList.value = it.getSuccessResult()
                }
                is Resource.Failure -> {
                    Log.d("card product list view model", "error")
                }
                else -> Unit
            }
        }
    }
}