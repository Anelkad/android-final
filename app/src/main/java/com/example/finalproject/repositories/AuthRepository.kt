package com.example.finalproject.repositories

import com.example.finalproject.models.User
import com.example.finalproject.utils.PRODUCTS
import com.example.finalproject.utils.Resource
import com.example.finalproject.utils.USERS
import java.lang.Exception
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
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    val auth: FirebaseAuth,
    val firebase: FirebaseDatabase
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signUp(email: String, password: String, user: User): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            firebase.getReference(USERS).child(currentUser!!.uid).setValue(user)
            auth.signOut()
            Resource.Success(result.user!!)
        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logOut(){
        auth.signOut()
    }

    override fun getCurrentUserDetails() = callbackFlow{
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user!=null) this@callbackFlow.trySendBlocking(Resource.Success(user))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Failure(error.toException()))
            }
        }

        firebase.getReference(USERS).child(currentUser!!.uid)
            .addValueEventListener(postListener)

        awaitClose {
            if (currentUser!=null)firebase.getReference(USERS).child(currentUser!!.uid)
                .removeEventListener(postListener)
        }
    }


}

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun logIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(email: String, password: String, user: User): Resource<FirebaseUser>
    fun logOut()
    fun getCurrentUserDetails(): Flow<Resource<User>>
}