package com.example.userblinkit.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartProducts")
data class CartProduct(

    @PrimaryKey
    val productId : String = "random",
    var productTitle : String ?= null,
    var productQuantity : String ?= null,
    var productPrice : Double ?= null,
    var productStock : Int ?= null,
    var productCategory : String ?= null,
    var itemCount : Int = 0,
    val adminUid : String ?= null,
    var productImageUris : String ?= null
)
