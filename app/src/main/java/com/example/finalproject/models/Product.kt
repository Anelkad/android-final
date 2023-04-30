package com.example.finalproject.models

class Product (
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val rating: Float = 0F
)