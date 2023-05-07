package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.ProductItemBinding
import com.example.finalproject.models.Product

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.HolderProduct> {

    lateinit var binding: ProductItemBinding
    var productList: ArrayList<Product>

    constructor(productList: ArrayList<Product>) : super() {
        this.productList = productList
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val price = binding.price
        val image = binding.imageView
        val addProduct = binding.addProductButton
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = ProductItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return HolderProduct(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduct, position: Int) {
        val product = productList[position]
        val id = product.id
        val title = product.title
        val price = product.price.toString()
        val imageUrl = product.imageUrl
        val rating = product.rating.toString()

        binding.title.text = title
        binding.price.text = "${price} тенге"
        binding.rating.text = "Рейтинг: ${rating}"

        Glide
            .with(holder.image.context)
            .load(imageUrl)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.image)

        holder.itemView.tag = product
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(id) } }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

}