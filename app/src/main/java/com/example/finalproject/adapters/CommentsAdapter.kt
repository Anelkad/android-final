package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.CommentItemBinding
import com.example.finalproject.models.Comment

class CommentsAdapter: RecyclerView.Adapter<CommentsAdapter.HolderProduct> {

    lateinit var binding: CommentItemBinding
    var comments: ArrayList<Comment>
    lateinit var imageAdapter: StarsAdapter

    constructor(list: ArrayList<Comment>) : super() {
        this.comments = list
    }

    inner class HolderProduct(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = binding.name
        val text = binding.text
        val imageList = binding.imageList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduct {
        binding = CommentItemBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false)
        return HolderProduct(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduct, position: Int) {
        val comment = comments[position]
        val name = comment.user_name
        val rating = comment.rating
        val text = comment.comment_text

        holder.name.text = name
        holder.text.text = text

        var stars = ArrayList<Int>()
        for (i in 1..rating) stars.add(i)

         imageAdapter = StarsAdapter(stars)

        val horizontalLayout = LinearLayoutManager(holder.imageList.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.imageList.layoutManager = horizontalLayout
        holder.imageList.adapter = imageAdapter

    }

    override fun getItemCount(): Int {
        return comments.size
    }

}