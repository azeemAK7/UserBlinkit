package com.example.userblinkit.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartProduct : CartProduct)

    @Update
    suspend fun updateCartProduct(cartProduct : CartProduct)

    @Query("DELETE FROM CartProducts WHERE productId = :productId")
    suspend fun deleteCartProduct(productId : String)

    @Query("SELECT * FROM CartProducts")
    fun getAllCartProducts() : LiveData<List<CartProduct>>
}