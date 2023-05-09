package com.example.finalproject.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class Purchase(): Serializable{
        var id: String = ""
        lateinit var date: String
        lateinit var products: ArrayList<CardProduct>
        var address: String = ""
        var totalCost: Int = 0

    constructor(
        products: ArrayList<CardProduct>,
        totalCost: Int
    ) : this() {
        this.products = products
        this.totalCost=totalCost
    }

    fun putId(id: String){
        this.id = id
    }

    fun putDate(){
        this.date = LocalDateTime.now().toString()
    }

    fun putAddress(address: String){
        this.address = address
    }

    }