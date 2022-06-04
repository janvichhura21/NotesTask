package com.example.task.dashboard

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.Network.Resource
import com.example.task.model.Notes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    val repository=HomeRepository()
    val resultData = MutableLiveData<Resource<String>?>()
    private val result = MutableLiveData<Resource<Notes>?>()
    private val orderDetails=MutableLiveData<List<Notes>?>()


    fun getNotes():MutableLiveData<List<Notes>?> {
        viewModelScope.launch {
            repository.getOrderDetails().collect{
                orderDetails.value=it.data
            }
        }
        return orderDetails
    }

    fun addNotes(notes: Notes): MutableLiveData<Resource<Notes>?> {
        if (Firebase.auth.currentUser != null) {
            viewModelScope.launch {
                repository.addNotes(notes)
                    .collect { resource ->
                        if (resource != null) {
                            result.value = resource
                            Resource.success(result)
                            Log.d("viewModel", "${resource.data?.id}")
                        } else {
                            resource?.message?.let { Resource.error(null, it) }
                        }
                    }
            }
        }
        return result
    }
    fun saveImageUrl(
        uri: Uri
    ): MutableLiveData<Resource<String>?> {
        if (Firebase.auth.currentUser != null) {
            viewModelScope.launch {
                repository.saveImage(uri).collect { resource ->
                    if (resource != null) {
                        resultData.value = resource

                        Resource.success(resultData)
                    } else {
                        resource?.message?.let { Resource.error(null, it) }
                    }
                }
            }
        }
        return resultData


    }
}