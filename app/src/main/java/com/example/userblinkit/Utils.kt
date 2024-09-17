package com.example.userblinkit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.userblinkit.databinding.FragmentSignInBinding
import com.example.userblinkit.databinding.ProgressDialogboxBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {
    private var dialog : AlertDialog ?= null

    fun ToastMes( context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    fun setStatusBarColor(activity: Activity,context: Context){
        activity?.window?.apply {
            val color = ContextCompat.getColor(context,R.color.Yellow)
            statusBarColor = color
        }
    }

    fun showDialog(context: Context,message:String){
        val progress = ProgressDialogboxBinding.inflate(LayoutInflater.from(context))
        progress.TVprogress.text = message
        dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog!!.show()
    }
    fun closeDialog(){
        dialog?.dismiss()
    }

    private var firebaseAuthInstance : FirebaseAuth ?= null

    fun getAuthInstance() : FirebaseAuth{
        if(firebaseAuthInstance==null){
            firebaseAuthInstance = FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!!
    }

    fun getCurrentUserId() : String{
        return  FirebaseAuth.getInstance().currentUser?.uid!!
    }


}