package com.example.userblinkit.Home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkit.Adapters.ProductAdapter
import com.example.userblinkit.CartLayout
import com.example.userblinkit.Models.Products
import com.example.userblinkit.R
import com.example.userblinkit.RoomDatabase.CartProduct
import com.example.userblinkit.Utils
import com.example.userblinkit.ViewModels.UserViewModel
import com.example.userblinkit.databinding.FragmentCategoryBinding
import com.example.userblinkit.databinding.ProductItemviewBinding
import kotlinx.coroutines.launch


class CategoryFragment : Fragment() {

    val userViewModel : UserViewModel by viewModels()
    private  var cartLayout: CartLayout ?= null
    private lateinit var adapter : ProductAdapter
    private lateinit var binding: FragmentCategoryBinding
    private var categoryTitle : String ?= null
    private lateinit var cardProduct: CartProduct

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        setTitle()
        onSearchClicked()
        getAllCategoryProduct()
        onBackBtnClicked()
        return binding.root
    }

    private fun onBackBtnClicked() {
        binding.tbCategory.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_categoryragment_to_homeFragment)
        }
    }

    private fun getAllCategoryProduct() {
        binding.productShimer.visibility = View.VISIBLE
        lifecycleScope.launch {
            userViewModel.getALlCategoeyProduct(categoryTitle!!).collect{listOfProduct ->
                if(listOfProduct.isEmpty()){
                    binding.tvNoProduct.visibility = View.VISIBLE
                    binding.rvProducts.visibility = View.GONE
                }else{
                    binding.tvNoProduct.visibility = View.GONE
                    binding.rvProducts.visibility = View.VISIBLE
                }
                adapter  = ProductAdapter(::onAddBtnClicked,::onIncrementBtnClicked,::onDecrementBtnClicked)
                binding.rvProducts.adapter = adapter
                adapter.differ.submitList(listOfProduct)
                binding.productShimer.visibility = View.GONE
            }
        }

    }

    private fun onSearchClicked() {
        binding.tbCategory.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.search ->{
                    findNavController().navigate(R.id.action_categoryragment_to_searchFragment)
                    true
                }else -> {
                    false
                }
            }
        }
    }

    private fun setTitle() {
        categoryTitle = arguments?.getString("category")
        binding.tbCategory.title = categoryTitle
    }

    private fun onAddBtnClicked(binding: ProductItemviewBinding,product: Products){
        binding.Add.visibility  = View.GONE
        binding.llItemCount.visibility = View.VISIBLE
        product.itemCount += 1
        binding.total.text = product.itemCount.toString()
        cartLayout?.showCartLayout(product.itemCount)
        cartLayout?.savingCartItemSP()
        lifecycleScope.launch {
            saveCartProdDb(product)
            userViewModel.insertCartProduct(cardProduct)
            userViewModel.updateProductsInFB(product)
        }
    }

    private fun onIncrementBtnClicked(binding: ProductItemviewBinding,product: Products){
        product.itemCount++
        if(product.productStock!! + 1 > product.itemCount){
            binding.total.text = product.itemCount.toString()
            cartLayout?.showCartLayout(1)
            cartLayout?.savingCartItemSP()
            lifecycleScope.launch {
                saveCartProdDb(product)
                userViewModel.updateCartProduct(cardProduct)
                userViewModel.updateProductsInFB(product)
            }
        }else{
            Utils.ToastMes(requireContext(),"max order reached product out of stock")
        }
    }

    private fun onDecrementBtnClicked(binding: ProductItemviewBinding,product: Products){
        product.itemCount--

        cartLayout?.showCartLayout(-1)
        cartLayout?.savingCartItemSP()
        lifecycleScope.launch {
            saveCartProdDb(product)
            userViewModel.updateCartProduct(cardProduct)
            userViewModel.updateProductsInFB(product)
        }

        if(product.itemCount >0){
            binding.total.text = product.itemCount.toString()
        }else{
            lifecycleScope.launch {
                userViewModel.deleteCartProduct(product.productId!!)
            }
            binding.Add.visibility  = View.VISIBLE
            binding.llItemCount.visibility = View.GONE
        }

    }

    private fun saveCartProdDb(product: Products) {
        cardProduct = CartProduct(
            productId = product.productId.toString(),
            productTitle = product.productTitle ,
            productQuantity = product.productQuantity.toString() + product.productUnit.toString() ,
            productPrice = product.productPrice ,
            productStock = product.productStock  ,
            productCategory = product.productCategory ,
            itemCount =  product.itemCount,
            adminUid = product.adminUid ,
            productImageUris = product.productImageUris.get(0)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CartLayout){
            cartLayout = context
        }
        else{
            throw ClassCastException("Please Implement Cart Listner")
        }
    }


}