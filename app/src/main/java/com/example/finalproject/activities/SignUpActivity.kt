package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.R
import com.example.finalproject.utils.Resource
import com.example.finalproject.databinding.ActivitySignUpBinding
import com.example.finalproject.models.User
import com.example.finalproject.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    lateinit var binding: ActivitySignUpBinding
    val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startLoginActivity()
        }

        binding.signUpButton.setOnClickListener {
            if (validateSignUpFields()){
                val user = User(
                    firstName,
                    lastName,
                    email
                )
                authViewModel.signUpUser(email, password, user)
            }
        }

        authViewModel.signupState.observe(this, Observer {
            when(it){
                is Resource.Failure -> {
                    hideWaitDialog()
                    //Log.d("qwerty", "sign up fail")
                    showSnackBar(it.exception.message.toString(),true)
                }
                is Resource.Loading -> {
                    showWaitDialog()
                    //Log.d("qwerty", "sign up loading")
                }
                is Resource.Success -> {
                    hideWaitDialog()
                    //Log.d("qwerty", "sign up success")
                    Toast.makeText(this, resources.getString(R.string.successSignUp),Toast.LENGTH_LONG)
                        .show()
                    startLoginActivity()
                }
                else -> Unit
            }
        })

    }

    private fun startLoginActivity(){
        val intent = Intent (this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateSignUpFields(): Boolean{
        firstName = binding.firstName.text.toString()
        lastName = binding.lastName.text.toString()
        email = binding.email.text.toString()
        password = binding.password.text.toString()
        confirmPassword = binding.confirmPassword.text.toString()

        return when{
            firstName.isEmpty() -> {
                showSnackBar(resources.getString(R.string.firstNameIsEmpty),true)
                false
            }
            lastName.isEmpty() -> {
                showSnackBar(resources.getString(R.string.lastNameIsEmpty),true)
                false
            }
            email.isEmpty() -> {
                showSnackBar(resources.getString(R.string.emailIsEmpty),true)
                false
            }
            password.isEmpty() -> {
                showSnackBar(resources.getString(R.string.passwordIsEmpty),true)
                false
            }
            confirmPassword.isEmpty() -> {
                showSnackBar(resources.getString(R.string.confirmPasswordIsEmpty),true)
                false
            }
            confirmPassword!= password -> {
                showSnackBar(resources.getString(R.string.passwordNotEqual),true)
                false
            }
            else -> {
                true
            }
        }
    }
}