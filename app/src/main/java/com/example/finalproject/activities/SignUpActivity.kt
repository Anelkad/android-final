package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivitySignUpBinding
import com.example.finalproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startLoginActivity()
        }

        binding.signUpButton.setOnClickListener {
            signUpUser()
        }
    }

    private fun startLoginActivity(){
        val intent = Intent (this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUpUser(){

        showWaitDialog()

        if (validateSignUpFields()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    hideWaitDialog()

                    if (it.isSuccessful) {
                        //val firebaseUser: FirebaseUser = it.result!!.user!!

                        val user = User(
                            email,
                            firstName,
                            lastName
                        )

                        //FirestoreClass().signupUser(this@SignUpActivity, user)

                        Toast.makeText(this@SignUpActivity,resources.getString(R.string.successSignUp),
                            Toast.LENGTH_LONG).show()

                        FirebaseAuth.getInstance().signOut()
                        startLoginActivity()

                    } else {
                        showSnackBar(it.exception!!.message.toString(), true)
                    }
                }

        }
    }

    private fun validateSignUpFields(): Boolean{
        firstName = binding.firstName.text.toString()
        lastName = binding.lastName.text.toString()
        email = binding.email.text.toString()
        password = binding.password.text.toString()
        confirmPassword = binding.confirmPassword.text.toString()

        return when{
            firstName.isEmpty() -> {
                hideWaitDialog()
                showSnackBar(resources.getString(R.string.firstNameIsEmpty),true)
                false
            }
            lastName.isEmpty() -> {
                hideWaitDialog()
                showSnackBar(resources.getString(R.string.lastNameIsEmpty),true)
                false
            }
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
            confirmPassword.isEmpty() -> {
                hideWaitDialog()
                showSnackBar(resources.getString(R.string.confirmPasswordIsEmpty),true)
                false
            }
            confirmPassword!= password -> {
                hideWaitDialog()
                showSnackBar(resources.getString(R.string.passwordNotEqual),true)
                false
            }
            else -> {
                //showSnackBar("Success",false)
                true
            }
        }
    }
}