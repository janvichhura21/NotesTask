package com.example.task.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )
        binding.root
        binding.edtProfilePic.isClickable=false
        binding.notesTitle.isCursorVisible=false
        binding.notesDes.isCursorVisible=false
        setUpFetchNotes()
    }
    private fun setUpFetchNotes() {
        val noteTitle= intent.getStringExtra("title")
        binding.notesTitle.setText(noteTitle)
        val noteDes= intent.getStringExtra("des")
        binding.notesDes.setText(noteDes)
        val url=intent.getStringExtra("image")
        Glide.with(this)
            .load(url)
            .into(binding.edtProfilePic)
    }
}