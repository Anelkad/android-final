package com.example.finalproject.repositories

import android.util.Log
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Product
import com.example.finalproject.utils.CARD
import com.example.finalproject.utils.PRODUCTS
import com.example.finalproject.utils.Resource
import com.example.finalproject.utils.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    val auth: FirebaseAuth,
    val firebase: FirebaseDatabase
): ProductRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser
    override suspend fun addProduct(product: Product): Resource<Product> {
        val database = firebase.getReference(PRODUCTS)
        val id = database.push().key
        product.putId(id!!)
        database.child(id).setValue(product).await()
        return Resource.Success(product)
    }

    override suspend fun getProductDetails(id: String): Resource<Product> {
        var resource: Resource<Product> = Resource.Loading
        try {
            firebase.getReference(PRODUCTS).child(id).get()
                .addOnSuccessListener {
                    val product = it.getValue(Product::class.java)!!
                        resource = Resource.Success(product)
                        Log.d("product repository", product.id)
                        Log.d("product repository", product.title)
                    }
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
                        //Log.d("repository product add", product.title)
                    }
                }
                this@callbackFlow.trySendBlocking(Resource.Success(productList))
                //Log.d("repository product list", productList.size.toString())
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
        Log.d("qwerty await close","done")
    }

    override fun getCardProductList() = callbackFlow{
        var productList = ArrayList<CardProduct>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (ds in snapshot.children) {
                    val product = ds.getValue(CardProduct::class.java)
                    if (product != null) {
                        productList.add(product)
                        //Log.d("repository card product add", product.title)
                    }
                }
                this@callbackFlow.trySendBlocking(Resource.Success(productList))
                //Log.d("repository card product list", productList.size.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
            }
        }
        firebase.getReference(USERS).child(currentUser!!.uid)
            .child(CARD).orderByChild("id")
            .addValueEventListener(postListener)

        awaitClose {
            firebase.getReference(USERS).child(CARD)
                .removeEventListener(postListener)
        }
        Log.d("qwerty await close","done")
    }

    override suspend fun addProductToCard(cardProduct: CardProduct): Resource<CardProduct> {
        var resource: Resource<CardProduct> = Resource.Loading

        try {
            val productRef = firebase.getReference(USERS)
                .child(currentUser!!.uid).child(CARD).child(cardProduct.id)

            productRef.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        cardProduct.addCount()
                    }
                    productRef.setValue(cardProduct)
                    resource = Resource.Success(cardProduct)
                }
                .await()
            }
        catch (e: Exception){
            e.printStackTrace()
            resource = Resource.Failure(e)
        }
        return resource
    }

    override suspend fun addCountCardProduct(id: String) {
        try {
            val productRef = firebase.getReference(USERS)
                .child(currentUser!!.uid).child(CARD).child(id)

            productRef.get()
                .addOnSuccessListener {
                    val product = it.getValue(CardProduct::class.java)!!
                    product.addCount()
                    productRef.setValue(product)
                    //Log.d("card product repository", product.id)
                    //Log.d("card product repository", product.title)
                }
                .await()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun removeCountCardProduct(id: String) {
        try {
            val productRef = firebase.getReference(USERS)
                .child(currentUser!!.uid).child(CARD).child(id)

            productRef.get()
                .addOnSuccessListener {
                    val product = it.getValue(CardProduct::class.java)!!
                    product.removeCount()
                    if (product.count<=0) productRef.removeValue()
                    else productRef.setValue(product)
                    // Log.d("card product repository", product.id)
                    //Log.d("card product repository", product.title)
                }
                .await()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

}

interface ProductRepository{

    val currentUser: FirebaseUser?
    suspend fun addProduct(product: Product): Resource<Product>
    suspend fun getProductDetails(id: String): Resource<Product>
    fun getProductList(): Flow<Resource<ArrayList<Product>>>
    fun getCardProductList(): Flow<Resource<ArrayList<CardProduct>>>
    suspend fun addProductToCard(product: CardProduct): Resource<CardProduct>
    suspend fun addCountCardProduct(id: String)
    suspend fun removeCountCardProduct(id: String)

}