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
import androidx.navigation.fragment.findNavController
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
    lateinit var binding: FragmentSellerBinding

    lateinit var title: String
    lateinit var description: String
    lateinit var imageUrl: String
    lateinit var price: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authViewModel.getCurrentUserDetails()

        binding = FragmentSellerBinding.inflate(inflater,container,false)

        authViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it==null
            if (it!=null) {
                val user = it
                binding.user.text = "${user.role}: ${user.email}"
                binding.sellerContent.isVisible = user.role == "admin" || user.role == "seller"
                binding.clientContent.isVisible = user.role == "client"
            }
        })

        binding.publishButton.setOnClickListener {
            if (validateProductFields()) publishProduct()
        }

        binding.logOut.setOnClickListener {
            authViewModel.logOut()
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.purchaseHistory.setOnClickListener {
            findNavController().navigate(R.id.action_sellerFragment_to_purchaseHistoryFragment)
        }

        return binding.root
    }

    private fun publishProduct(){
        val productViewModel by viewModels<ProductViewModel>()
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