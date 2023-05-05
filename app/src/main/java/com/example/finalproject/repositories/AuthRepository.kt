package com.example.finalproject.repositories

import com.example.finalproject.utils.Resource
import java.lang.Exception
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    val auth: FirebaseAuth
): AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signUp(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
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


}

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun logIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(email: String, password: String): Resource<FirebaseUser>
    fun logOut()
}