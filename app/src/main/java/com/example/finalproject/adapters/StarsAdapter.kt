package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.databinding.StarImageItemBinding
import com.example.finalproject.models.CardProduct

class StarsAdapter: RecyclerView.Adapter<StarsAdapter.HolderProduct> {

    lateinit var binding: StarImageItemBinding
    var list: ArrayList<Int>

    constructor(list: ArrayList<Int>) : super() {
        this.list = list
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = StarImageItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return HolderProduct(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduct, position: Int) {
        val star = list[position]
        holder.image.setImageResource(R.drawable.baseline_star_24)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}