package com.example.userblinkit.Auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.userblinkit.Models.Users
import com.example.userblinkit.R
import com.example.userblinkit.Utils
import com.example.userblinkit.ViewModels.AuthViewModel
import com.example.userblinkit.activity.UserActivity
import com.example.userblinkit.databinding.FragmentOTPBinding
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {
    private val authViewModel : AuthViewModel by viewModels()
    private lateinit var binding : FragmentOTPBinding
    private lateinit var mobileNumber : String
    private lateinit var ETarray : Array<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(layoutInflater)
        onNavigationIconClicked()
        setUserNumber()
        sendOtp()
        setETFocus()
        onLoginBtnClicked()
        return binding.root
    }

    private fun sendOtp() {
        Utils.showDialog(requireContext(),"Sending OTP")
        authViewModel.apply {
            sendOtp(mobileNumber,requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if(it){
                        Utils.closeDialog()
                        Utils.ToastMes(requireContext(),"Otp Sent")
                    }
                }
            }

        }
    }

    private fun onLoginBtnClicked() {
        binding.btnLogin.setOnClickListener {
            val otp = ETarray.joinToString(separator = "") { it.text.toString() }
            if(otp.length < ETarray.size){
                Utils.ToastMes(requireContext(),"Incorrect Otp")
            }else{
                ETarray.forEach { it.text.clear() ; it.clearFocus() }
                Utils.showDialog(requireContext(),"varifying otp")
                varifyOtp(otp)
            }
        }
    }

    private fun varifyOtp(otp: String) {
        authViewModel.apply {
            val user = Users(userNumber = mobileNumber)
            signInWithPhoneAuthCredential(otp,user)
            lifecycleScope.launch {
                isSuccessful.collect{
                    if(it){
                        Utils.closeDialog()
                        startActivity(Intent(requireActivity(), UserActivity::class.java))
                        requireActivity().finish()
                        }
                    }
                }
            }
        }

    private fun setETFocus() {
        ETarray =  arrayOf(binding.ETOTP1,binding.ETOTP2,binding.ETOTP3,binding.ETOTP4,binding.ETOTP5,binding.ETOTP6)
        ETarray.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(number: Editable?) {
                    when {
                        number?.length == 1 && index < ETarray.size - 1 -> ETarray[index + 1].requestFocus()
                        number?.length == 0 && index > 0 -> ETarray[index - 1].requestFocus()
                    }
                }
            })
        }
    }

    private fun setUserNumber() {
        val bundle = arguments
        mobileNumber = bundle?.getString("number").toString()
        binding.tvUserMobileNumber.text = mobileNumber
    }

    private fun onNavigationIconClicked() {
        binding.OTPToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }


}