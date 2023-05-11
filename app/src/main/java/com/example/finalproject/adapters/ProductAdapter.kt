package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.FilterProduct
import com.example.finalproject.R
import com.example.finalproject.databinding.ProductItemBinding
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Product

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.HolderProduct>, Filterable {

    lateinit var binding: ProductItemBinding
    var productList: ArrayList<Product>
    var filterList: ArrayList<Product>

    private var filter: FilterProduct? = null

    constructor(productList: ArrayList<Product>) : super() {
        this.productList = productList
        this.filterList = productList
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val price = binding.price
        val rating = binding.rating
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
        val price = product.price
        val imageUrl = product.imageUrl
        val rating = product.rating.toString()

        holder.title.text = title
        holder.price.text = "${price} тенге"
        holder.rating.text = "Рейтинг: ${rating}"

        Glide
            .with(holder.image.context)
            .load(imageUrl)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.image)

        holder.itemView.setOnClickListener { onItemClickListener?.let { it(id) } }
        holder.addProduct.setOnClickListener {
            onButtonClickListener?.let { it(CardProduct(id,title,imageUrl,price)) }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    private var onButtonClickListener: ((CardProduct) -> Unit)? = null

    fun setOnButtonClickListener(listener: (CardProduct) -> Unit) {
        onButtonClickListener = listener
    }

    override fun getFilter(): Filter {
        if (filter==null){
            filter = FilterProduct(filterList,this)
        }
        return filter as FilterProduct
    }

}