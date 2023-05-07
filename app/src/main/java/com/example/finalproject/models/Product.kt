package com.example.finalproject.models

class Product{
    var title: String = ""
    var description: String = ""
    var imageUrl: String = ""
    var price: Int = 0
    var rating: Float = 0F
    var comments_count: Int = 0
    var id: String = ""
    constructor()

    constructor(
        title: String,
        description: String,
        imageUrl: String,
        price: Int
    ){
        this.title = title
        this.description = description
        this.imageUrl = imageUrl
        this.price = price
    }
    fun putId(id: String){
        this.id = id
    }
}