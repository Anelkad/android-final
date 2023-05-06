package com.example.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.finalproject.R
import com.example.finalproject.activities.LoginActivity
import com.example.finalproject.databinding.FragmentSellerBinding
import com.example.finalproject.models.User
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerFragment : Fragment(R.layout.fragment_seller) {

    val authViewModel by viewModels<AuthViewModel>()
    lateinit var binding: FragmentSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        authViewModel.getCurrentUserDetails()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSellerBinding.bind(view)

        authViewModel.currentUserState.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    Log.d("qwerty seller", "current user fail")
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.sellerContent.isVisible = false
                    Log.d("qwerty seller", "current user load")
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val user = it.getSuccessResult()
                    Log.d("qwerty seller", user.email)
                    binding.user.text = "${user.role}: ${user.email}"
                    binding.sellerContent.isVisible = user.role == "admin"
                }
                else -> Unit
            }
        })

        binding.logOut.setOnClickListener {
            authViewModel.logOut()
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}