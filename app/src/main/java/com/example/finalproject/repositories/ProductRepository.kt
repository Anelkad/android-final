package com.example.finalproject.repositories

import android.util.Log
import com.example.finalproject.models.Product
import com.example.finalproject.utils.PRODUCTS
import com.example.finalproject.utils.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
   val firebase: FirebaseDatabase
): ProductRepository {

    override suspend fun addProduct(product: Product): Resource<Product> {
        val database = firebase.getReference(PRODUCTS)
        val id = database.push().key
        product.putId(id!!)
        database.child(id).setValue(product).await()
        return Resource.Success(product)
    }

    override suspend fun getProduct(id: String): Resource<Product> {
        var resource: Resource<Product> = Resource.Loading
        try {
            firebase.getReference(PRODUCTS).child(id).get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val product = it.getValue(Product::class.java)!!
                        resource = Resource.Success(product)
                    }}
                .await()
        }
        catch (e: Exception){
            e.printStackTrace()
            resource = Resource.Failure(e)
        }
        return resource
    }

    override fun getProductList() = callbackFlow{
        var productList = ArrayList<Product>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (ds in snapshot.children) {
                    val product = ds.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                        Log.d("repository product add", product.title)
                    }
                }
                this@callbackFlow.trySendBlocking(Resource.Success(productList))
                Log.d("repository product list", productList.size.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
            }
        }
        firebase.getReference(PRODUCTS).
            addValueEventListener(postListener)

        awaitClose {
            firebase.getReference(PRODUCTS)
                .removeEventListener(postListener)
        }
    }

}

interface ProductRepository{
    suspend fun addProduct(product: Product): Resource<Product>
    suspend fun getProduct(id: String): Resource<Product>
    fun getProductList(): Flow<Resource<ArrayList<Product>>>

}