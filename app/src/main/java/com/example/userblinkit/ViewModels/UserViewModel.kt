package com.example.userblinkit.ViewModels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userblinkit.Models.Products
import com.example.userblinkit.RoomDatabase.CartProduct
import com.example.userblinkit.RoomDatabase.CartProductDB
import com.example.userblinkit.RoomDatabase.CartProductDao
import com.example.userblinkit.Utils
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {


    val sharedPreference : SharedPreferences = application.getSharedPreferences("MY_Pref",MODE_PRIVATE)

    val cartProductDao : CartProductDao  = CartProductDB.getDatabaseInstance(application).cartProductDAO()

    private val _isUserAddSaved = MutableStateFlow(false)
    val isUserAddSaved = _isUserAddSaved



    fun getAllProducts() : Flow<List<Products>>  = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts")
        val  productList = ArrayList<Products>()

        val eventListner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (product in snapshot.children){
                    val prod = product.getValue(Products::class.java)
                    productList.add(prod!!)
                }
                trySend(productList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        db.addValueEventListener(eventListner)
        awaitClose { db.removeEventListener(eventListner) }
    }

    fun getALlCategoeyProduct(category: String) : Flow<List<Products>>  = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${category}")
        val  productList = ArrayList<Products>()
        val eventListner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (product in snapshot.children){
                    val prod = product.getValue(Products::class.java)
                    productList.add(prod!!)
                }
                trySend(productList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        db.addValueEventListener(eventListner)
        awaitClose { db.removeEventListener(eventListner) }

    }

    fun updateProductsInFB(product: Products){
            FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts/${product.productId}").child("itemCount") .setValue(product.itemCount)
                FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${product.productCategory}/${product.productId}").child("itemCount") .setValue(product.itemCount)
                    FirebaseDatabase.getInstance().getReference("Admins").child("ProductType/${product.productType}/${product.productId}").child("itemCount") .setValue(product.itemCount)
        }

    fun saveUserAddINFB(address : String){
        FirebaseDatabase.getInstance().reference.child("AllUsers").child("Users").child(Utils.getCurrentUserId()).child("userAddress").setValue(address).addOnSuccessListener {
            _isUserAddSaved.value = true
        }
    }


    //shared preference
    fun getUserAddStatus() : MutableLiveData<Boolean> {
        val status = MutableLiveData<Boolean>()
        status.value = sharedPreference.getBoolean("Address",false)
        return status
    }

    fun saveUserAddStatus() {
        sharedPreference.edit().putBoolean("Address",true)
    }


    fun savingCartItemSP(itemCount : Int){
        sharedPreference.edit().putInt("itemCount",itemCount).apply()
    }

    //room


    fun insertCartProduct(cartProduct: CartProduct){
        viewModelScope.launch {
            cartProductDao.insertCartProduct(cartProduct)
        }
    }

    fun updateCartProduct(cartProduct: CartProduct){
        viewModelScope.launch {
            cartProductDao.updateCartProduct(cartProduct)
        }
    }

    fun deleteCartProduct(productId : String){
        viewModelScope.launch {
            cartProductDao.deleteCartProduct(productId)
        }
    }

    fun getAllCartProducts() : LiveData<List<CartProduct>>{
        return cartProductDao.getAllCartProducts()
    }


}