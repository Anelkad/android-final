package com.example.finalproject.repositories

import com.example.finalproject.models.Product
import com.example.finalproject.utils.PRODUCTS
import com.example.finalproject.utils.Resource
import com.example.finalproject.utils.USERS
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
   val firebase: FirebaseDatabase
): ProductRepository {

    override suspend fun addProduct(product: Product): Resource<Product> {
        val database = firebase.getReference(PRODUCTS)
        val id = database.push().key
        database.child(id!!).setValue(product).await()
        return Resource.Success(product)
    }

}

interface ProductRepository{
    suspend fun addProduct(product: Product): Resource<Product>

}