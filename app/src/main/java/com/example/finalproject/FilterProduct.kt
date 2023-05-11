package com.example.finalproject

import android.widget.Filter
import com.example.finalproject.adapters.ProductAdapter
import com.example.finalproject.models.Product

class FilterProduct : Filter {

    val filterList: ArrayList<Product>

    val adapter: ProductAdapter

    constructor(filterList: ArrayList<Product>, adapter: ProductAdapter) {
        this.filterList = filterList
        this.adapter = adapter
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()

        if (constraint!=null && constraint.isNotEmpty()){
            constraint = constraint.toString().lowercase()
            var filteredProducts = ArrayList<Product>()
            for (i in filterList.indices){
                if (filterList[i].title.lowercase().contains(constraint)){
                    filteredProducts.add(filterList[i])
                }
            }
            results.count = filteredProducts.size
            results.values = filteredProducts
        }
        else{
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapter.productList = results.values as ArrayList<Product>
        adapter.notifyDataSetChanged()
    }


}