package com.example.finalproject.models

class CardProduct {
    var id: String = ""
    var title: String = ""
    var imageUrl: String = ""
    var price: Int = 0
    var count: Int = 1

    constructor()

    constructor(
        id: String,
        title: String,
        imageUrl: String,
        price: Int
    ){
        this.id = id
        this.title = title
        this.imageUrl = imageUrl
        this.price = price
    }

    constructor(
        product: Product
    ){
        this.id = product.id
        this.title = product.title
        this.imageUrl = product.imageUrl
        this.price = product.price
    }

    fun addCount() = this.count++

    fun removeCount() = this.count--
}