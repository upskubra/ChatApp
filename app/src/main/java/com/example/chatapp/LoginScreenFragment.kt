package com.example.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginScreenFragment : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser !=null){
            val action = LoginScreenFragmentDirections.actionLoginScreenFragmentToChatScreenFragment()
            findNavController().navigate(action)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.logInButton.setOnClickListener {

            auth.signInWithEmailAndPassword(binding.TextEmailAddress.text.toString(), binding.TextPassword.text.toString())
                .addOnSuccessListener {

                    val action = LoginScreenFragmentDirections.actionLoginScreenFragmentToChatScreenFragment()
                    findNavController().navigate(action)




                }.addOnFailureListener { Exception ->


                    println(binding.TextEmailAddress.toString())
                    context?.let {

                        Toast.makeText(it.applicationContext,Exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }



                }


        }

        binding.signInButton.setOnClickListener {

            auth.createUserWithEmailAndPassword(binding.TextEmailAddress.text.toString(), binding.TextPassword.text.toString())
                .addOnSuccessListener {

                    val action = LoginScreenFragmentDirections.actionLoginScreenFragmentToChatScreenFragment()
                    findNavController().navigate(action)


                }.addOnFailureListener { Exception ->


                    println(binding.TextEmailAddress.toString())
                    context?.let {

                        Toast.makeText(it.applicationContext,Exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }


                }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}