package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.ProductImageItemBinding
import com.example.finalproject.databinding.PurchaseProductItemBinding
import com.example.finalproject.models.CardProduct

class ImageProductAdapter: RecyclerView.Adapter<ImageProductAdapter.HolderProduct> {

    lateinit var binding: ProductImageItemBinding
    var productList: ArrayList<CardProduct>

    constructor(productList: ArrayList<CardProduct>) : super() {
        this.productList = productList
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = ProductImageItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return HolderProduct(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduct, position: Int) {
        val product = productList[position]
        val imageUrl = product.imageUrl

        Glide
            .with(holder.image.context)
            .load(imageUrl)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}