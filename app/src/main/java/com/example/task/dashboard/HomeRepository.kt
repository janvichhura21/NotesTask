package com.example.task.dashboard

import android.net.Uri
import android.util.Log
import com.example.task.Network.BaseAPI
import com.example.task.Network.Resource
import com.example.task.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class HomeRepository:BaseAPI() {

    fun getOrderDetails()= callbackFlow<Resource<List<Notes>?>>{
        FirebaseAuth.getInstance().uid?.let { uid->
            val query= db.collection("Notes")
                .whereEqualTo("id",uid)
                .get()
            query.addOnSuccessListener {
                trySend(Resource.success(it.toObjects(Notes::class.java)))
                Log.d("viewModel", "${it.toObjects(Notes::class.java)}")

            }
                .addOnFailureListener { exception ->

                    Log.w("viewModel", "Error getting documents: ", exception)
                }
        }
        awaitClose {}
    }

    fun addNotes(notes: Notes) = callbackFlow<Resource<Notes>?> {
        FirebaseAuth.getInstance().uid?.let { uid ->
            db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
            db.collection("Notes")
                .document()
                .set(notes)
                .addOnCompleteListener {
                    trySend(Resource.success(notes))
                }
                .addOnFailureListener {
                    trySend(Resource.error(null, it.localizedMessage))
                }
        }
        awaitClose {}
    }
    fun saveImage(imageUri: Uri) = callbackFlow<Resource<String>?> {
        uploadImage(
            getPathString("notes-pic", imageUri),
            imageUri,
            { url ->
                Log.d("janvi", "$url")
                trySend(Resource.success(url))
            },
            {
                trySend(Resource.error(null, it.localizedMessage))

            })
        awaitClose {}
    }
     fun uploadPhotos(imageUri: Uri)= callbackFlow<Resource<String>?> {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val profilePicStorageRef =
            storageReference.child(getPath("pic",imageUri))
        val uploadTask = profilePicStorageRef.putFile(imageUri)
        uploadTask.addOnFailureListener {
            Log.d("error document", "${it.message}")

        }.addOnSuccessListener {
            Log.d("image upload", "${it.metadata}")
            val downloadUrl = profilePicStorageRef.downloadUrl
            downloadUrl.addOnSuccessListener { imageUrl ->
                Log.d("image", imageUrl.toString())
                val notes=Notes()
                  notes.imageUrl = imageUrl.toString()

            }

        }
         awaitClose {  }
    }
    protected fun getPath(folderName: String, imageUri: Uri) =
        "${folderName}/${FirebaseAuth.getInstance().uid}/${imageUri.lastPathSegment}"


    fun upload(imageUri: Uri)= callbackFlow<Resource<String>?> {
        val storageReference=FirebaseStorage.getInstance().getReference("images/${FirebaseAuth.getInstance().uid}")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                Log.d("image upload", "${it.metadata}")
            }
            .addOnFailureListener{
                Log.d("error document", "${it.message}")
            }
    }

}