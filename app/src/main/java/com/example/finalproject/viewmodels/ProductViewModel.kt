package com.example.finalproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.models.Product
import com.example.finalproject.repositories.ProductRepositoryImp
import com.example.finalproject.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepositoryImp
): ViewModel() {

    private val _addProductState = MutableLiveData<Resource<Product>?>(null)
    val addProductState: LiveData<Resource<Product>?> = _addProductState

    fun addProduct(product: Product) = viewModelScope.launch {
        _addProductState.value = Resource.Loading
        val result = repository.addProduct(product)
        _addProductState.value = result
    }

}