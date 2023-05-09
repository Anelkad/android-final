package com.example.finalproject.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalproject.R
import com.example.finalproject.adapters.PurchaseProductAdapter
import com.example.finalproject.databinding.FragmentConfirmPurchaseBinding
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Purchase
import com.example.finalproject.utils.Resource
import com.example.finalproject.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPurchaseFragment : BaseFragment() {

    lateinit var binding: FragmentConfirmPurchaseBinding
    val arg: ConfirmPurchaseFragmentArgs by navArgs()
    lateinit var productAdapter: PurchaseProductAdapter
    lateinit var purchase: Purchase

    val productViewModel by viewModels<ProductViewModel>()

    private lateinit var address: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.d("qwerty confirm", arg.purchase.totalCost.toString())
        purchase = arg.purchase

        val productList = purchase.products
        productAdapter = PurchaseProductAdapter(productList)

        binding = FragmentConfirmPurchaseBinding.inflate(inflater,container,false)
        binding.listView.adapter = productAdapter
        binding.totalCost.text = "Итого к оплате: ${ purchase.totalCost } тенге"

        binding.confirmPurchaseButton.setOnClickListener {
            if (validateAddressField()){
                purchase.putDate()
                purchase.putAddress(address)
                productViewModel.purchase(purchase)
                productViewModel.purchaseState.observe(viewLifecycleOwner, Observer{
                    when(it){
                        is Resource.Failure -> {
                            //hideWaitDialog()
                            showSnackBar("Can't make purchase!",true)
                        }
                        is Resource.Loading -> {
                            //showWaitDialog()
                        }
                        is Resource.Success -> {
                            //hideWaitDialog()
                            productViewModel.clearCard()
                            showSnackBar("Success purchase!",false)
                            findNavController().popBackStack()
                        }
                        else -> Unit
                    }
                })
            }
        }

        return binding.root
    }

    private fun validateAddressField(): Boolean{
        address = binding.address.text.toString()

        return when{
            address.isEmpty() -> {
                showSnackBar("Please enter address field!",true)
                false
            }
            else -> {
                true
            }
        }
    }
}