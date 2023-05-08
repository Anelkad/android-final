package com.example.finalproject.repositories

import android.util.Log
import com.example.finalproject.models.User
import com.example.finalproject.utils.Resource
import com.example.finalproject.utils.USERS
import java.lang.Exception
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    override suspend fun getCurrentUserDetails(): Resource<User> {
        var resource: Resource<User> = Resource.Loading
        try {
        firebase.getReference(USERS).child(currentUser!!.uid).get()
            .addOnSuccessListener {
                if (it.exists()){val user = it.getValue(User::class.java)!!
                resource = Resource.Success(user)
            }}
            .await()
        }
        catch (e: Exception){
            e.printStackTrace()
            resource = Resource.Failure(e)
        }
        return resource
    }


}

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun logIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(email: String, password: String, user: User): Resource<FirebaseUser>
    fun logOut()
    suspend fun getCurrentUserDetails(): Resource<User>
}