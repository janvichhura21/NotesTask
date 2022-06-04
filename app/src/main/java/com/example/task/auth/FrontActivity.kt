package com.example.task.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.task.R
import com.example.task.dashboard.HomeActivity
import com.example.task.databinding.ActivityFrontBinding
import com.example.task.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class FrontActivity : AppCompatActivity() {
    lateinit var binding: ActivityFrontBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_front
        )
        binding.root
        FirebaseAuth.getInstance().currentUser?.let {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        replaceFragment(R.id.frame,FrontFragment())
    }
}
