package com.example.task.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.task.Network.Utils
import com.example.task.R
import com.example.task.dashboard.HomeActivity
import com.example.task.databinding.FragmentSignupBinding
import com.google.android.material.textfield.TextInputLayout

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    lateinit var viewModel: LoginViewModel
    private val requiredFields: List<TextInputLayout> by lazy {
        listOf(
            binding.layoutEmail,
            binding.layoutPassword,
            binding.layoutPhone,
            binding.layoutName
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        binding.signup.setOnClickListener {
            if (!Utils.isAnyFieldEmpty(requiredFields)) {
                val name = binding.edtName.text.toString()
                val email = binding.edtEmail.text.toString()
                val phone = binding.edtNumber.text.toString()
                val password = binding.edtPassword.text.toString()
                if (isValidPassword(password)) {
                    viewModel.getSignUp(name, phone, email, password)
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else{
                    binding.edtPassword.error="password must contain 8 character \n some special charcter [%^&*$#@!]\n and upperCase character[A-Z] \nlower case [a-z] "
                }
            }
        }
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