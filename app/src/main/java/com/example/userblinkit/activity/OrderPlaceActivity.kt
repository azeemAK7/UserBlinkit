package com.example.userblinkit.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.userblinkit.Adapters.CartAdapter
import com.example.userblinkit.R
import com.example.userblinkit.Utils
import com.example.userblinkit.ViewModels.UserViewModel
import com.example.userblinkit.databinding.ActivityOrderPlaceBinding
import com.example.userblinkit.databinding.AddressLayoutBinding
import kotlinx.coroutines.launch

class OrderPlaceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOrderPlaceBinding
    private val userViewModel : UserViewModel by viewModels()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductToCheckout()
        onBackBtnClicked()
        onPlaceOrderClicked()
    }

    private fun onPlaceOrderClicked() {

        binding.navCheckout.setOnClickListener {
            userViewModel.getUserAddStatus().observe(this){status ->
                if(status){

                }else{
                    val addressLayout = AddressLayoutBinding.inflate(LayoutInflater.from(this))
                    val alertDialog = AlertDialog.Builder(this)
                        .setView(addressLayout.root)
                        .create()
                    alertDialog.show()

                    addressLayout.Add.setOnClickListener {
                        saveUserAddress(alertDialog,addressLayout)
                    }
                }
            }
        }

    }

    private fun saveUserAddress( alertDialog: AlertDialog, addressLayout: AddressLayoutBinding) {
            Utils.showDialog(this,"saving Address")
            lifecycleScope.launch {
                val pinCode = addressLayout.ETpincode.text.toString().trim()
                val phoneNo = addressLayout.ETPhoneNo.text.toString().trim()
                val state = addressLayout.ETState.text.toString().trim()
                val district = addressLayout.District.text.toString().trim()
                val userAddress = addressLayout.ETdescriptiveAddress.text.toString().trim()


                val address = "$pinCode $district($state) $userAddress $phoneNo"
                userViewModel.saveUserAddINFB(address)
                userViewModel.saveUserAddStatus()
                lifecycleScope.launch {
                    userViewModel.isUserAddSaved.collect{it ->
                        if(it){
                            Utils.closeDialog()
                            alertDialog.dismiss()
                        }
                    }
                }

            }

    }

    private fun onBackBtnClicked() {
        binding.CheckoutToolbar.setNavigationOnClickListener {
            startActivity(Intent(this,UserActivity::class.java))
            finish()
        }
    }

    private fun getProductToCheckout() {
        userViewModel.getAllCartProducts().observe(this){cartProduct ->
            adapter = CartAdapter()
            binding.RvShowProduct.adapter = adapter
            adapter.differ.submitList(cartProduct)

            var totalPrice = 0.0

            for(product in cartProduct){
                val productPrice = product.productPrice
                val productCount = product.itemCount.toDouble()
                totalPrice += (productPrice?.times(productCount)!!)
            }
            binding.subTotalPrice.text = totalPrice.toString()

            if(totalPrice < 200){
                binding.deliveryChargePrice.text = "$15"
                totalPrice += 15
            }

            binding.grandtotalPrice.text = totalPrice.toString()

        }
    }
}