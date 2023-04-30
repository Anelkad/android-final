package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener{
            startSignUpActivity()
        }

        binding.loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        showWaitDialog()

        if (validateLoginFields()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {

                    hideWaitDialog()

                    if (it.isSuccessful) {
                        //FirestoreClass().getUserDetails(this@LoginActivity)
                        Toast.makeText(this,resources.getString(R.string.successLogIn),
                            Toast.LENGTH_LONG).show()
                        startMainActivity()

                    } else {
                        showSnackBar(it.exception!!.message.toString(),true)
                    }
                }
        }
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
                hideWaitDialog()
                showSnackBar(resources.getString(R.string.emailIsEmpty),true)
                false
            }
            password.isEmpty() -> {
                hideWaitDialog()
                showSnackBar(resources.getString(R.string.passwordIsEmpty),true)
                false
            }
            else -> {
                //showSnackBar("Success",false)
                true
            }
        }
    }



}