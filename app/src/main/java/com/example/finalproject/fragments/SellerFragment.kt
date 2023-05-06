package com.example.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.R
import com.example.finalproject.activities.LoginActivity
import com.example.finalproject.databinding.FragmentSellerBinding
import com.example.finalproject.models.Product
import com.example.finalproject.models.User
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.AuthViewModel
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerFragment : BaseFragment(){

    val authViewModel by viewModels<AuthViewModel>()
    val productViewModel by viewModels<ProductViewModel>()
    lateinit var binding: FragmentSellerBinding

    lateinit var title: String
    lateinit var description: String
    lateinit var imageUrl: String
    lateinit var price: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authViewModel.getCurrentUserDetails()
        return inflater.inflate(R.layout.fragment_seller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSellerBinding.bind(view)

        authViewModel.currentUserState.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    //Log.d("qwerty seller", "current user fail")
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.sellerContent.isVisible = false
                    //Log.d("qwerty seller", "current user load")
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val user = it.getSuccessResult()
                    //Log.d("qwerty seller", user.email)
                    binding.user.text = "${user.role}: ${user.email}"
                    binding.sellerContent.isVisible = user.role == "admin"
                }
                else -> Unit
            }
        })

        binding.publishButton.setOnClickListener {
            if (validateProductFields()){
                val product = Product(
                    title,
                    description,
                    imageUrl,
                    price.toInt()
                )
                productViewModel.addProduct(product)
                productViewModel.addProductState.observe(viewLifecycleOwner, Observer{
                    when(it){
                        is Resource.Failure -> {
                            hideWaitDialog()
                            showSnackBar("Can't add product!",true)
                        }
                        is Resource.Loading -> {
                            showWaitDialog()
                        }
                        is Resource.Success -> {
                            hideWaitDialog()
                            clearFields()
                            showSnackBar("Product \"${product.title}\" added!",false)
                        }
                        else -> Unit
                    }
                })
            }
        }

        binding.logOut.setOnClickListener {
            authViewModel.logOut()
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun validateProductFields(): Boolean{
        title = binding.title.text.toString()
        description = binding.description.text.toString()
        imageUrl = binding.image.text.toString()
        price = binding.price.text.toString()

        return when{
            title.isEmpty() -> {
                showSnackBar("Please enter title!",true)
                false
            }
            description.isEmpty() -> {
                showSnackBar("Please enter description!",true)
                false
            }
            imageUrl.isEmpty() -> {
                showSnackBar("Please enter image Url!",true)
                false
            }
            price.isEmpty() -> {
                showSnackBar("Please enter price!",true)
                false
            }
            else -> {
                //showSnackBar("Success",false)
                true
            }
        }
    }

    private fun clearFields(){
        binding.title.text.clear()
        binding.description.text.clear()
        binding.image.text.clear()
        binding.price.text.clear()
    }

}