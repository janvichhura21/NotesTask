package com.example.task.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.task.R
import com.example.task.dashboard.HomeActivity
import com.example.task.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        replaceFragment(R.id.frame,SignupFragment())
       /* FirebaseAuth.getInstance().currentUser?.let {
           val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }*/
    }
}