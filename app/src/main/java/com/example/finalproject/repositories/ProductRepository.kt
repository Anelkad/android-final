package com.example.finalproject.repositories

import android.util.Log
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Comment
import com.example.finalproject.models.Product
import com.example.finalproject.models.Purchase
import com.example.finalproject.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
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

    override fun getProductDetails(id: String) = callbackFlow{

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)
                if (product!=null) this@callbackFlow.trySendBlocking(Resource.Success(product))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
            }
        }

        firebase.getReference(PRODUCTS).child(id).addValueEventListener(postListener)

        awaitClose {
            firebase.getReference(PRODUCTS).child(id)
                .removeEventListener(postListener)
        }

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

    override suspend fun purchase(purchase: Purchase): Resource<Purchase> {
        var resource: Resource<Purchase> = Resource.Loading

        try {
            val database = firebase.getReference(USERS)
                .child(currentUser!!.uid).child(PURCHASE)
            val id = database.push().key
            purchase.putId(id!!)
            resource = Resource.Success(purchase)
            database.child(id).setValue(purchase).await()
        }
        catch (e: Exception){
            e.printStackTrace()
            resource = Resource.Failure(e)
        }
        return resource
    }

    override fun getPurchaseList() = callbackFlow{
        var purchaseList = ArrayList<Purchase>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                purchaseList.clear()
                for (ds in snapshot.children) {
                    val purchase = ds.getValue(Purchase::class.java)
                    if (purchase != null) {
                        purchaseList.add(purchase)
                        //Log.d("repository card product add", product.title)
                    }
                }
                this@callbackFlow.trySendBlocking(Resource.Success(purchaseList))
                //Log.d("repository card product list", productList.size.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
            }
        }
        firebase.getReference(USERS).child(currentUser!!.uid)
            .child(PURCHASE).orderByChild("id")
            .addValueEventListener(postListener)

        awaitClose {
            firebase.getReference(USERS).child(currentUser!!.uid)
                .child(PURCHASE).removeEventListener(postListener)
        }
        Log.d("qwerty await close","done")
    }

    override fun getCommentsList(product_id: String) = callbackFlow{
        var comments = ArrayList<Comment>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.clear()
                for (ds in snapshot.children) {
                    val comment = ds.getValue(Comment::class.java)
                    if (comment != null) {
                        comments.add(comment)
                        //Log.d("repository card product add", product.title)
                    }
                }
                this@callbackFlow.trySendBlocking(Resource.Success(comments))
                //Log.d("repository card product list", productList.size.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
            }
        }
        firebase.getReference(PRODUCTS).child(product_id)
            .child(COMMENTS).orderByChild("user_id")
            .addValueEventListener(postListener)

        awaitClose {
            firebase.getReference(PRODUCTS).child(product_id)
                .child(COMMENTS)
                .removeEventListener(postListener)
        }
        Log.d("qwerty await close","done")
    }

    override suspend fun addComment(comment: Comment, product_id: String): Resource<Comment> {
        var resource: Resource<Comment> = Resource.Loading
        var rating: Float = 0F
        var commentsCount: Int = 0
        comment.putId(currentUser!!.uid)

        try {
            val ratingRef = firebase.getReference(PRODUCTS)
                .child(product_id).child("rating")

            ratingRef.get().addOnSuccessListener {
                rating = it.getValue(Float::class.java)!!
                Log.d("qwerty rating", rating.toString())
            }

            val commentsCountRef = firebase.getReference(PRODUCTS)
                .child(product_id).child("comments_count")

            commentsCountRef.get().addOnSuccessListener {
                commentsCount = it.getValue(Int::class.java)!!
                Log.d("qwerty comments_count", commentsCount.toString())
            }

            val commentRef = firebase.getReference(PRODUCTS)
                .child(product_id).child(COMMENTS).child(currentUser!!.uid)

            commentRef.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val replaceRating = (rating*commentsCount - rating + comment.rating)/commentsCount
                        ratingRef.setValue(replaceRating)
                    }else{
                        val newCommentsCount = commentsCount + 1
                        val newRating = (rating*commentsCount + comment.rating)/newCommentsCount
                        commentsCountRef.setValue(newCommentsCount)
                        ratingRef.setValue(newRating)
                    }
                    commentRef.setValue(comment)
                    resource = Resource.Success(comment)
                }.await()
        }
        catch (e: Exception){
            e.printStackTrace()
            resource = Resource.Failure(e)
        }
        return resource
    }

    override suspend fun clearCard() {
        firebase.getReference(USERS)
            .child(currentUser!!.uid).child(CARD).removeValue().await()
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
    fun getProductDetails(id: String): Flow<Resource<Product>>
    fun getProductList(): Flow<Resource<ArrayList<Product>>>
    fun getCardProductList(): Flow<Resource<ArrayList<CardProduct>>>
    suspend fun addProductToCard(product: CardProduct): Resource<CardProduct>
    suspend fun purchase(purchase: Purchase): Resource<Purchase>
    fun getPurchaseList(): Flow<Resource<ArrayList<Purchase>>>
    fun getCommentsList(product_id: String): Flow<Resource<ArrayList<Comment>>>
    suspend fun addComment(comment: Comment, product_id: String): Resource<Comment>
    suspend fun clearCard()
    suspend fun addCountCardProduct(id: String)
    suspend fun removeCountCardProduct(id: String)

}