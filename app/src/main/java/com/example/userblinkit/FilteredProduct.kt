package com.example.userblinkit

import android.widget.Filter
import com.example.userblinkit.Adapters.ProductAdapter
import com.example.userblinkit.Models.Products
import java.util.Locale

class FilteredProduct(
    val adapter : ProductAdapter,
    val productList :  ArrayList<Products>

) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()
        val filteredList = ArrayList<Products>()
        val query = constraint.toString().trim().uppercase(Locale.getDefault()).split(" ")
        if(query != null){
            for (product in productList){
                if(query.any {
                        product.productTitle?.uppercase(Locale.getDefault())?.contains(it) == true || product.productCategory?.uppercase(Locale.getDefault())?.contains(it) == true || product.productPrice.toString().uppercase(Locale.getDefault()).contains(it) == true || product.productType?.uppercase(Locale.getDefault())?.contains(it) == true
                    }){
                    filteredList.add(product)
                }
            }
            result.values = filteredList
            result.count = filteredList.size
        }else{
            result.values = productList
            result.count = productList.size
        }

        return  result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
       adapter.differ.submitList(results?.values as ArrayList<Products>)
    }


}