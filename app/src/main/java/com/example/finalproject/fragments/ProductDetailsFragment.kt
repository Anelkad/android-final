package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.FragmentProductDetailsBinding
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding

    val productViewModel by viewModels<ProductViewModel>()
    val arg: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productViewModel.getProduct(arg.id)
        Log.d("product detail fragment", arg.id)

        binding = FragmentProductDetailsBinding.inflate(inflater,container, false)

        productViewModel.productDetailState.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val product = it.getSuccessResult()
                    binding.title.text = product.title
                    binding.id.text = product.id
                    binding.price.text = product.price.toString()
                    binding.rating.text = product.rating.toString()

                    Glide
                        .with(this)
                        .load(product.imageUrl)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.baseline_image_not_supported_24)
                        .into(binding.imageView)
                }
                else -> Unit
            }
        })

        return binding.root
    }
}