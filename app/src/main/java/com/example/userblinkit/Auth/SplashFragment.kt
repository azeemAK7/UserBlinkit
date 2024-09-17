package com.example.userblinkit.Auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkit.R
import com.example.userblinkit.Utils
import com.example.userblinkit.activity.UserActivity
import com.example.userblinkit.databinding.FragmentSplashBinding
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater)
        Handler(Looper.getMainLooper()).postDelayed({

            lifecycleScope.launch {
                if(Utils.getAuthInstance().currentUser?.uid.isNullOrBlank()){
                    findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                }else{
                    startActivity(Intent(requireActivity(), UserActivity::class.java))
                    requireActivity().finish()
                }
            }
        },3000)
        return binding.root
    }
}

