package com.example.finalproject.models


class Comment {
    var user_id: String = ""
    var user_name: String = ""
    var rating: Int = 0
    var comment_text: String = ""

    constructor()

    constructor(
        user_name: String,
        rating: Int,
        comment_text: String
    ){
        this.user_name = user_name
        this.rating = rating
        this.comment_text = comment_text
    }

    fun putId(id: String){
        this.user_id = id
    }

}