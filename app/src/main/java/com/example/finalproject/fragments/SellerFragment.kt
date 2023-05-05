package com.example.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.example.finalproject.R
import com.example.finalproject.activities.LoginActivity
import com.example.finalproject.databinding.FragmentSellerBinding
import com.example.finalproject.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerFragment : Fragment(R.layout.fragment_seller) {

    val authViewModel by viewModels<AuthViewModel>()
    lateinit var binding: FragmentSellerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSellerBinding.bind(view)

        binding.logOut.setOnClickListener {
            authViewModel.logOut()
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}