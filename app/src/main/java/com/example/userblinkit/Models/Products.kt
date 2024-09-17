package com.example.userblinkit.Models

data class Products(
    val productId : String ?= null,
    var productTitle : String ?= null,
    var productQuantity : Int ?= null,
    var productUnit : String ?= null,
    var productPrice : Double ?= null,
    var productStock : Int ?= null,
    var productCategory : String ?= null,
    var productType : String ?= null,
    var itemCount : Int = 0,
    val adminUid : String ?= null,
    var productImageUris : ArrayList<String?> = arrayListOf()

)
