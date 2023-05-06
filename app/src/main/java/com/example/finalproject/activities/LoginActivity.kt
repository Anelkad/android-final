package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.R
import com.example.finalproject.utils.Resource
import com.example.finalproject.databinding.ActivityLoginBinding
import com.example.finalproject.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener{
            startSignUpActivity()
        }

        binding.loginButton.setOnClickListener {
            if (validateLoginFields()){
                authViewModel.logInUser(email,password)
            }
        }

        authViewModel.loginState.observe(this, Observer {
            when(it){
                is Resource.Failure -> {
                    if (isDialogInit()) hideWaitDialog()
                    //Log.d("qwerty", "login fail")
                    showSnackBar(it.exception.message.toString(),true)
                }
                is Resource.Loading -> {
                    showWaitDialog()
                    //Log.d("qwerty", "login load")
                }
                is Resource.Success -> {
                    if (isDialogInit()) hideWaitDialog()
                    val user = it.getSuccessResult()
                    //Log.d("qwerty login", user.email!!)
                    Toast.makeText(this, resources.getString(R.string.successLogIn),Toast.LENGTH_LONG)
                        .show()
                    startMainActivity()
                }
                else -> Unit
            }
        })
    }

    private fun startSignUpActivity(){
        val intent = Intent (this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMainActivity(){
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateLoginFields(): Boolean{
        email = binding.email.text.toString()
        password = binding.password.text.toString()

        return when{
            email.isEmpty() -> {
                showSnackBar(resources.getString(R.string.emailIsEmpty),true)
                false
            }
            password.isEmpty() -> {
                showSnackBar(resources.getString(R.string.passwordIsEmpty),true)
                false
            }
            else -> {
                true
            }
        }
    }



}