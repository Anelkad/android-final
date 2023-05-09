package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.PurchaseItemBinding
import com.example.finalproject.models.CardProduct
import com.example.finalproject.models.Purchase

class PurchaseHistoryAdapter: RecyclerView.Adapter<PurchaseHistoryAdapter.HolderProduct> {

    lateinit var binding: PurchaseItemBinding
    var purchaseList: ArrayList<Purchase>
    lateinit var imageAdapter: ImageProductAdapter

    constructor(list: ArrayList<Purchase>) : super() {
        this.purchaseList = list
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val date = binding.date
        val description = binding.description
        val imageList = binding.imageList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = PurchaseItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return HolderProduct(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduct, position: Int) {
        val product = purchaseList[position]
        val id = product.id
        val date = product.date
        val products = product.products
        val totalCost = product.totalCost
        val address = product.address

        holder.date.text = date
        holder.description.text = "Адрес:${address}  Оплачено: ${totalCost} тенге"

        imageAdapter = ImageProductAdapter(products)

        val horizontalLayout = LinearLayoutManager(holder.imageList.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.imageList.layoutManager = horizontalLayout
        holder.imageList.adapter = imageAdapter

    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }



}