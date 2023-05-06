package com.example.finalproject.models

class Product (
    val title: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val rating: Float = 0F,
    val comments_count: Int = 0
)