package com.example.userblinkit.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userblinkit.Models.Category
import com.example.userblinkit.databinding.ItemviewCategoryBinding
import kotlin.reflect.KFunction1

class categoryAdapter(val categoryList: ArrayList<Category>,val onCategoryClicked: (String) -> Unit) : RecyclerView.Adapter<categoryAdapter.categoryViewHolder>(){


    class categoryViewHolder(val binding : ItemviewCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryViewHolder {
        return categoryViewHolder(ItemviewCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return categoryList.size
    }

    override fun onBindViewHolder(holder: categoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            imgCategory.setImageResource(category.img)
            titleCategory.text = category.title
        }
        holder.binding.itemViewMain.setOnClickListener {
            onCategoryClicked(category.title.toString())
        }
    }
}