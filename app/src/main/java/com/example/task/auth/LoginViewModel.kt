package com.example.task.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel:ViewModel() {

    val firebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
    fun getSignUp(name:String,phone:String,email:String,password:String){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d("signUp","SuccessFully SignUp")
            }
            .addOnFailureListener {
                Log.d("error","UnSuccessFully SignUp",it)
            }
    }

    fun getLogin(email: String,password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("Login","SuccessFully Login")
            }
            .addOnFailureListener {
                Log.d("error","unSuccessFully Login")
            }
    }
}