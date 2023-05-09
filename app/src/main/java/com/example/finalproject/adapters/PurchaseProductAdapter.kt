package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.PurchaseProductItemBinding
import com.example.finalproject.models.CardProduct

class PurchaseProductAdapter: RecyclerView.Adapter<PurchaseProductAdapter.HolderProduct> {

    lateinit var binding: PurchaseProductItemBinding
    var productList: ArrayList<CardProduct>

    constructor(productList: ArrayList<CardProduct>) : super() {
        this.productList = productList
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val price = binding.price
        val image = binding.imageView
        val count = binding.count
        val totalCount = binding.totalCount
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = PurchaseProductItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return HolderProduct(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduct, position: Int) {
        val product = productList[position]
        val id = product.id
        val title = product.title
        val price = product.price
        val count = product.count
        val imageUrl = product.imageUrl

        holder.title.text = title
        holder.price.text = "${price} тенге"
        holder.count.text = "${count} x"

        holder.totalCount.text = "${price*count}тг"

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