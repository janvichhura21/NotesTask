package com.example.task.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.task.Network.Utils
import com.example.task.R
import com.example.task.dashboard.HomeActivity
import com.example.task.databinding.FragmentLoginBinding
import com.example.task.setRequired
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {
    lateinit var viewModel: LoginViewModel
    lateinit var binding: FragmentLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private val requiredFields: List<TextInputLayout> by lazy {
        listOf(
            binding.layoutEmail,
            binding.layoutPassword
        )
    }
    private val requiredField: List<TextInputLayout> by lazy {
        listOf(
            binding.layoutPhone
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()
        viewModel=ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        binding.loginbtn.setOnClickListener {
          if (!Utils.isAnyFieldEmpty(requiredFields)||!Utils.isAnyFieldEmpty(requiredField)) {
              val phonenumber = binding.edtPhoneNo.text.toString()
              val email = binding.edtemail.text.toString()
              val password = binding.edtPassword.text.toString()
              if (phonenumber.isBlank()) {
                  if (isValidPassword(password)) {
                       viewModel.getLogin(email, password)
                      val intent= Intent(requireActivity(), HomeActivity::class.java)
                      startActivity(intent)
                      activity?.finish()
                   }
                  else{
                      binding.edtPassword.error="password must contain 8 character \n some special charcter [%^&*$#@!]\n and upperCase character[A-Z] \nlower case [a-z] "
                  }
              }


              if (email.isBlank() && password.isBlank()) {
                  if (phonenumber.isBlank()) {

                      Toast.makeText(
                          requireActivity(),
                          "enter phone number or mail id and pass",
                          Toast.LENGTH_SHORT
                      ).show()
                  } else {
                      Toast.makeText(requireActivity(), "+91$phonenumber", Toast.LENGTH_SHORT)
                          .show()
                      sendVerificationcode("+91$phonenumber")
                  }
              }

           }
            requiredFields.forEach {
                it.setRequired()
            }
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(requireActivity(), OtpActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("number",(binding.edtPhoneNo.toString().trim()))
                startActivity(intent)
            }
        }
    }
    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    fun isValidPassword(password:String?):Boolean{
        password?.let {
            val passwordPattern = "^(?=.[0-9])(?=.*[a-z](?=.*[@#%^&+])(?=\\S+$).{4,}$)"
            val passwordMatcher = Regex(passwordPattern)
            return passwordMatcher.find(password) != null
        }

        return false
    }

}




