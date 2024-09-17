package com.example.userblinkit.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userblinkit.RoomDatabase.CartProduct
import com.example.userblinkit.databinding.BottomSheetCartItemviewBinding

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    val diffUtil = object  : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    class CartViewHolder(val binding: BottomSheetCartItemviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(BottomSheetCartItemviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(currentItem.productImageUris).into(ivProductImage)
            tvProductTitle.text = currentItem.productTitle
            tvProductQuantity.text = currentItem.productQuantity
            tvProductprice.text = currentItem.productPrice.toString()
            tvProductCount.text = currentItem.itemCount.toString()
        }
    }
}