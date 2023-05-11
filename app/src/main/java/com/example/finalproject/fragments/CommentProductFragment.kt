package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.FragmentCommentProductBinding
import com.example.finalproject.models.Comment
import com.example.finalproject.models.Product
import com.example.finalproject.models.User
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.AuthViewModel
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentProductFragment : BaseFragment() {

    lateinit var binding: FragmentCommentProductBinding
    lateinit var currentUser: User
    lateinit var product: Product

    lateinit var commentText: String
    lateinit var rating: String

    val productViewModel by viewModels<ProductViewModel>()
    val authViewModel by viewModels<AuthViewModel>()
    val arg: CommentProductFragmentArgs by navArgs()

    private fun validateCommentFields(): Boolean {
        commentText = binding.text.text.toString()

        return when{
            commentText.isEmpty() -> {
                showSnackBar("Please enter text!",true)
                false
            }
            binding.radioGroup.checkedRadioButtonId == -1 -> {
                showSnackBar("Please select rating!",true)
                false
            }
            else -> {
                //showSnackBar("Success",false)
                true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productViewModel.getProductDetails(arg.productId)
        authViewModel.getCurrentUserDetails()

        binding = FragmentCommentProductBinding.inflate(inflater,container,false)

        productViewModel.productDetail.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it==null
            if (it!=null) {
                product = it
                binding.productTitle.text = product.title
                binding.description.text = product.description
                Glide
                    .with(this)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.baseline_image_not_supported_24)
                    .into(binding.imageView)
            }
        })

        authViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it==null
            if (it!=null) {
                currentUser = it
            }
        })

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = requireView().findViewById(checkedId)
            rating = radio.text.toString()
        }

        binding.addCommentButton.setOnClickListener {
            if (validateCommentFields()){
                Log.d("qwerty", product.id)
                Log.d("qwerty", currentUser.firstName)

                val name = "${currentUser.lastName} ${currentUser.firstName}"
                val comment = Comment(name, rating.toInt(), commentText)

                productViewModel.addComment(comment, product.id)
                productViewModel.addCommentState.observe(viewLifecycleOwner, Observer{
                    when(it){
                        is Resource.Failure -> {
                            hideWaitDialog()
                            showSnackBar("Can't add comment!",true)
                        }
                        is Resource.Loading -> {
                            showWaitDialog()
                        }
                        is Resource.Success -> {
                            hideWaitDialog()
                            showSnackBar("Comment added!",false)
                            findNavController().popBackStack()
                        }
                        else -> Unit
                    }
                })

            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}