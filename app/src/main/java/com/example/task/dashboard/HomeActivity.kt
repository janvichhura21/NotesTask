package com.example.task.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.addnotes.AddNotesActivity
import com.example.task.auth.FrontActivity
import com.example.task.auth.SignUpActivity
import com.example.task.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )
        binding.root
        homeAdapter= HomeAdapter(this)
        viewModel=ViewModelProvider(this)[HomeViewModel::class.java]
        binding.imageLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,FrontActivity::class.java))
            finish()
        }
        setUpRv()
        setUpNotes()
    }


    private fun setUpNotes() {
        binding.addBtn.setOnClickListener {
            val intent=Intent(this,AddNotesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpRv() {
        binding.rv.apply {
            layoutManager=LinearLayoutManager(this@HomeActivity)
            adapter=homeAdapter
            viewModel.getNotes().observe(this@HomeActivity, Observer {
                if (it != null) {
                    homeAdapter.deliveryDetails=it
                }
            })
        }
    }
}