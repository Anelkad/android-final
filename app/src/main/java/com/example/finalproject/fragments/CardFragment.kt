package com.example.finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.finalproject.R
import com.example.finalproject.databinding.FragmentCardBinding

class CardFragment : Fragment(R.layout.fragment_card) {
lateinit var binding: FragmentCardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCardBinding.bind(view)

        binding.purchaseButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_cardFragment_to_confirmPurchaseFragment)
        }

    }
}