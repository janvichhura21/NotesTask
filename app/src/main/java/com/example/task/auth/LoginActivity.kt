package com.example.task.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.task.R
import com.example.task.replaceFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        replaceFragment(R.id.frame,LoginFragment())
    }
}