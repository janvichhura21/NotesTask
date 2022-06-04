package com.example.task.Network

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*


/**
 * This class contains a reference of Firestore data base as db
 * and some functions for basic CRUD operations
 *
 * Every API class must extend this BaseAPI class
 */

open class BaseAPI {

    private val TAG = BaseAPI::class.java.simpleName
    protected val db = FirebaseFirestore.getInstance()

    protected fun deleteDocument(
        collectionName: String,
        documentId: String,
        onSuccess: (Void?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionName)
            .document(documentId)
            .delete()
            .addOnSuccessListener { void ->
                Log.d(TAG, "Document $documentId deleted from $collectionName")
                onSuccess(void)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Failed to delete document $documentId from $collectionName")
                onFailure(exception)
            }
    }

    protected fun uploadImage(
        pathString: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val profilePicStorageRef =
            storageReference.child(pathString)
        val uploadTask = profilePicStorageRef.putFile(imageUri)
        uploadTask.addOnFailureListener {
            Log.d(TAG, "${it.message}")
            onFailure(it)
        }.addOnSuccessListener {
            Log.d(TAG, "${it.metadata}")
            val downloadUrl = profilePicStorageRef.downloadUrl
            downloadUrl.addOnSuccessListener { imageUrl ->
                Log.d(TAG, imageUrl.toString())
                onSuccess(imageUrl.toString())
            }
        }
    }

    protected fun getPathString(folderName: String, imageUri: Uri) =
        "${folderName}/${FirebaseAuth.getInstance().uid}/${imageUri.lastPathSegment}"
}
