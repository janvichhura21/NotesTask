package com.example.task.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.task.R
import com.example.task.databinding.FragmentFrontBinding

class FrontFragment : Fragment() {
    lateinit var binding:FragmentFrontBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_front, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInid.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
        binding.signupid.setOnClickListener {
            startActivity(Intent(requireActivity(), SignUpActivity::class.java))
            activity?.finish()
        }
    }
}