package com.example.finalproject.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.models.User
import com.example.finalproject.utils.Resource
import com.example.finalproject.repositories.AuthRepositoryImp
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImp
    ): ViewModel() {

    private val _loginState = MutableLiveData<Resource<FirebaseUser>?>(null)
    val loginState: LiveData<Resource<FirebaseUser>?> = _loginState

    private val _signupState = MutableLiveData<Resource<FirebaseUser>?>(null)
    val signupState: LiveData<Resource<FirebaseUser>?> = _signupState

    private val _currentUserState = MutableLiveData<Resource<User>?>(null)
    val currentUserState: LiveData<Resource<User>?> = _currentUserState

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginState.value = Resource.Success(repository.currentUser!!)
        }
    }
    fun getCurrentUserDetails() = viewModelScope.launch {
        _currentUserState.value = Resource.Loading
        val result = repository.getCurrentUserDetails()
        _currentUserState.value = result
    }

    fun logInUser(email: String, password: String) = viewModelScope.launch {
        _loginState.value = Resource.Loading
        val result = repository.logIn(email, password)
        _loginState.value = result
    }

    fun signUpUser(email: String, password: String, user: User) = viewModelScope.launch {
        _signupState.value = Resource.Loading
        val result = repository.signUp(email, password, user)
        _signupState.value = result
    }

    fun logOut() {
        repository.logOut()
        _loginState.value = null
        _signupState.value = null
    }

}