package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.finalproject.models.CardProduct
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment() {

    lateinit var binding: FragmentProductDetailsBinding
    val arg: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val productViewModel by viewModels<ProductViewModel>()

        productViewModel.getProductDetails(arg.id)
        Log.d("product detail fragment", arg.id)

        binding = FragmentProductDetailsBinding.inflate(inflater,container, false)
        //todo productDetailState loading зависает через раз
        productViewModel.productDetailState.observe(viewLifecycleOwner, Observer { resource ->
            Log.d("product detail state", resource.toString())
            when(resource){
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    Log.d("progress bar", "visible")
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val product = resource.getSuccessResult()
                    binding.productDetails.isVisible = true
                    binding.title.text = product.title
                    binding.description.text = product.description
                    binding.price.text = "Цена: ${ product.price } тг"
                    binding.rating.text = "Рейтинг: ${product.rating}"

                    Glide
                        .with(this)
                        .load(product.imageUrl)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.baseline_image_not_supported_24)
                        .into(binding.imageView)

                    binding.addToCard.setOnClickListener {
                        productViewModel.addProductToCard(CardProduct(product))
                        productViewModel.addProductToCardState.observe(viewLifecycleOwner, Observer{ cardProductResource ->
                            when(cardProductResource){
                                is Resource.Failure -> {
                                    //hideWaitDialog()
                                    showSnackBar("Can't add product!",true)
                                }
                                is Resource.Loading -> {
                                    //showWaitDialog()
                                }
                                is Resource.Success -> {
                                    //hideWaitDialog()
                                    showSnackBar("Product ${cardProductResource.getSuccessResult().title} added to card!",false)
                                }
                                else -> Unit
                            }
                        })
                    }

                }
                else -> Unit
            }
        })

        return binding.root
    }
}