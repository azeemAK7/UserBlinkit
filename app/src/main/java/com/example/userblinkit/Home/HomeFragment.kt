package com.example.userblinkit.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userblinkit.Adapters.categoryAdapter
import com.example.userblinkit.Constants
import com.example.userblinkit.Models.Category

import com.example.userblinkit.R
import com.example.userblinkit.databinding.FragmentHomeBinding


class homeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setCategoryProducts()
        navigateToSearchFrag()

        return binding.root
    }

    private fun onCategoryClicked(category: String) {
        val bundle = Bundle()
        bundle.putString("category",category)
        findNavController().navigate(R.id.action_homeFragment_to_categoryragment,bundle)
    }

    private fun navigateToSearchFrag() {
        binding.searchCV.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }


    private  fun setCategoryProducts() {
        val categoryList = ArrayList<Category>()
        for(i in 0 until Constants.allProductsCategoryTitle.size){
            categoryList.add(Category(Constants.allProductsCategoryIcon[i],Constants.allProductsCategoryTitle[i]))
        }
        binding.rvShopByCat.adapter = categoryAdapter(categoryList,::onCategoryClicked)
    }

}