package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.CardItemBinding
import com.example.finalproject.databinding.ProductItemBinding
import com.example.finalproject.models.CardProduct

class CardProductAdapter: RecyclerView.Adapter<CardProductAdapter.HolderProduct> {

    lateinit var binding: CardItemBinding
    var productList: ArrayList<CardProduct>

    constructor(productList: ArrayList<CardProduct>) : super() {
        this.productList = productList
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = binding.title
        val price = binding.price
        val image = binding.imageView
        val count = binding.count
        val addCount = binding.countAddButton
        val removeCount = binding.countRemoveButton
        val totalCount = binding.totalCount
        val itemView = binding.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = CardItemBinding.inflate(
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
        holder.count.text = count.toString()

        holder.totalCount.text = "${price*count}тг"

        Glide
            .with(holder.image.context)
            .load(imageUrl)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.image)

        //holder.itemView.setOnClickListener { onItemClickListener?.let { it(id) } }
        holder.addCount.setOnClickListener { addCountClickListener?.let{it(id)} }
        holder.removeCount.setOnClickListener { removeCountClickListener?.let{it(id)} }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    private var addCountClickListener: ((String) -> Unit)? = null
    private var removeCountClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnAddButtonClickListener(listener: (String) -> Unit) {
        addCountClickListener = listener
    }

    fun setOnRemoveButtonClickListener(listener: (String) -> Unit) {
        removeCountClickListener = listener
    }


}