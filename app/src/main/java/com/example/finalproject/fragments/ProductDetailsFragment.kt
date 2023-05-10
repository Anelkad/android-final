package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.adapters.CommentsAdapter
import com.example.finalproject.databinding.FragmentProductDetailsBinding
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Comment
import com.example.finalproject.models.Product
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment() {

    lateinit var binding: FragmentProductDetailsBinding
    val arg: ProductDetailsFragmentArgs by navArgs()

    lateinit var commentAdapter: CommentsAdapter
    lateinit var commentsList: ArrayList<Comment>

    val productViewModel by viewModels<ProductViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productViewModel.getProductDetails(arg.id)
        productViewModel.getCommentList(arg.id)

        commentsList = ArrayList()
        commentAdapter = CommentsAdapter(commentsList)

        binding = FragmentProductDetailsBinding.inflate(inflater,container, false)

        binding.commentListView.adapter = commentAdapter

        productViewModel.productDetail.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it==null
            if (it!=null) {
                val product = it
                fillProductBinding(product)
                binding.addToCard.setOnClickListener {
                    addProductToCardObserver(product)
                }
                binding.addCommentButton.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("product_id", product.id)
                    }
                    findNavController()
                        .navigate(R.id.action_productDetailsFragment_to_commentProductFragment,bundle)
                }

            }
        })

        productViewModel.commentList.observe(viewLifecycleOwner, Observer {
            commentsList.clear()
            if (it!=null){
                commentsList.addAll(it)
            }
            commentAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun fillProductBinding(product: Product){
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
    }
    private fun addProductToCardObserver(product: Product) {
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