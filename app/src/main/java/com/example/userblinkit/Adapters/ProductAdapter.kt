package com.example.userblinkit.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.userblinkit.FilteredProduct
import com.example.userblinkit.Models.Products
import com.example.userblinkit.databinding.ProductItemviewBinding
import kotlin.reflect.KFunction2

class ProductAdapter(val onAddBtnClicked: KFunction2<ProductItemviewBinding, Products, Unit>, val onIncrementBtnClicked: KFunction2<ProductItemviewBinding, Products, Unit>, val onDecrementBtnClicked: KFunction2<ProductItemviewBinding, Products, Unit>) : RecyclerView.Adapter<ProductAdapter.productViewHolder>() , Filterable{


    class productViewHolder(val binding: ProductItemviewBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    val diffUtil = object  : DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {

            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productViewHolder {
        return productViewHolder(ProductItemviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: productViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()

            for(i in 0 until product.productImageUris.size){
                imageList.add(SlideModel(product.productImageUris[i].toString()))
            }
            imageSlider.setImageList(imageList)
            productTitle.text = product.productTitle
            productPrice.text = "₹${product.productPrice.toString()}"
            val quantity = product.productQuantity.toString() + product.productUnit
            productQuantity.text = quantity

            if(product.itemCount >0){
                Add.visibility  = View.GONE
                llItemCount.visibility = View.VISIBLE
                total.text = product.itemCount.toString()
            }

            Add.setOnClickListener {
                onAddBtnClicked( this,product)
            }

            addBtn.setOnClickListener {
                onIncrementBtnClicked(this,product)
            }

            subBtn.setOnClickListener {
                onDecrementBtnClicked(this,product)
            }
        }


    }

    val filter : FilteredProduct ? = null
    var originalList = ArrayList<Products>()

    override fun getFilter(): Filter {
        if(filter == null) return FilteredProduct(this,originalList)
        return filter
    }

}