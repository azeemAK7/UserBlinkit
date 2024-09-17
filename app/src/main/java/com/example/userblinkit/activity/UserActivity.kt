package com.example.userblinkit.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.userblinkit.Adapters.CartAdapter
import com.example.userblinkit.CartLayout
import com.example.userblinkit.Models.Products
import com.example.userblinkit.R
import com.example.userblinkit.RoomDatabase.CartProduct
import com.example.userblinkit.ViewModels.UserViewModel
import com.example.userblinkit.databinding.ActivityUserBinding
import com.example.userblinkit.databinding.BottomSheetCartProductBinding
import com.example.userblinkit.databinding.ProductItemviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserActivity : AppCompatActivity() ,CartLayout {

    private lateinit var cartAdapter : CartAdapter
    private lateinit var cartProductList : List<CartProduct>
    private lateinit var binding: ActivityUserBinding
    val userViewModel : UserViewModel by viewModels()
    var itemCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        hideSystemUI()
        getUserItemInCart()
        getUserCartProduct()
        onBottomCartClicked()
        setContentView(binding.root)
        onNextBtnClicked()
    }

    private fun onNextBtnClicked(){
        binding.navCheckout.setOnClickListener {
            startActivity(Intent(this,OrderPlaceActivity::class.java))
            finish()
        }
    }

    private fun getUserCartProduct(){
        userViewModel.getAllCartProducts().observe(this){
            cartProductList = it
        }
    }

    private fun onBottomCartClicked() {
        binding.bottomViewCart.setOnClickListener {
            val bottomSheet = BottomSheetCartProductBinding.inflate(LayoutInflater.from(this))
            val bs = BottomSheetDialog(this)
            bs.setContentView(bottomSheet.root)

            cartAdapter = CartAdapter()
            bottomSheet.bottomItemCount.text = binding.itemCount.text.toString()
            bottomSheet.rvProductsItems.adapter = cartAdapter
            cartAdapter.differ.submitList(cartProductList)

            bottomSheet.navCheckout.setOnClickListener {
                startActivity(Intent(this,OrderPlaceActivity::class.java))
                finish()
            }

            bs.show()



        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    fun getUserItemInCart(){
        itemCount = userViewModel.sharedPreference.getInt("itemCount",0)
        if(itemCount > 0){
            binding.itemCount.text = itemCount.toString()
            binding.cart.visibility = View.VISIBLE
        }
        else{
            binding.cart.visibility = View.GONE
        }
    }

    override fun showCartLayout(count : Int) {
        binding.cart.visibility = View.VISIBLE

        itemCount += count
        if(itemCount>0){
            binding.itemCount.text = itemCount.toString()
        }else{
            itemCount = 0
            binding.itemCount.text = itemCount.toString()
            binding.cart.visibility = View.GONE
        }

    }

    override fun savingCartItemSP() {
        userViewModel.savingCartItemSP(itemCount)
    }


}