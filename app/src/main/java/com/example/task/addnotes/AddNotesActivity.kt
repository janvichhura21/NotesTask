package com.example.task.addnotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.dashboard.HomeActivity
import com.example.task.dashboard.HomeViewModel
import com.example.task.databinding.ActivityAddNotesBinding
import com.example.task.model.Notes
import com.google.firebase.auth.FirebaseAuth

class AddNotesActivity : AppCompatActivity() {
    lateinit var viewModel: HomeViewModel
    lateinit var binding: ActivityAddNotesBinding
    var uri: Uri? = null
    companion object{
        val IMAGE_REQUEST_CODE=100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_add_notes
        )
        binding.root

        viewModel=ViewModelProvider(this)[HomeViewModel::class.java]
        setListeners()
        setUpNotes()
     //  setUpFetchNotes()
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

    fun setListeners(){
        binding.edtImageprofile.setOnClickListener {
            if (binding.edtProfilePic.isEnabled) {
                picImage()
            }
        }
    }

    private fun picImage() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== IMAGE_REQUEST_CODE&&resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uri = data?.data!!
            binding.edtProfilePic.setImageURI(uri)
           viewModel.saveImageUrl(uri!!)
            //   binding.progressLayout.visibility= View.VISIBLE
        }
    }



    private fun setUpNotes() {
        binding.saveBtn.setOnClickListener {
            val notes=Notes().apply {
                id=FirebaseAuth.getInstance().uid
                title=binding.notesTitle.text.toString()
                description=binding.notesDes.text.toString()

            }
            viewModel.resultData.observe(this@AddNotesActivity, Observer {
                Log.d("cura",it?.data.toString())
                notes.imageUrl=it?.data.toString()
                Log.d("conaa",notes.imageUrl.toString())
            })
            viewModel.addNotes(notes)

                startActivity(Intent(this,HomeActivity::class.java))
                finish()

        }
    }
}