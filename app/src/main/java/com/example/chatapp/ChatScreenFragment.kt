package com.example.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.FragmentChatScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatScreenFragment : Fragment() {
    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!


    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    val db = Firebase.firestore

    private lateinit var adapter: ChatRecyclerAdapter
    private var chats = arrayListOf<Chat>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ini
        firestore = Firebase.firestore
        auth = Firebase.auth



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ChatRecyclerAdapter()
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.sendButton.setOnClickListener {


            auth.currentUser?.let {

                val user = it.email
                val message = binding.messageText.text.toString()
                val date = FieldValue.serverTimestamp()

                val dataMap = HashMap<String, Any>()

                dataMap["user"] = user!!
                dataMap["chat"] = message
                dataMap["time"] = date

                firestore.collection("Chats").add(dataMap).addOnSuccessListener {
                    binding.messageText.setText(" ")

                }.addOnFailureListener { exception ->

                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()

                    binding.messageText.setText(" ")

                }


            }


        }

        firestore.collection("Chats").orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG)
                        .show()


                } else
                    if (value != null) {


                        if (value.isEmpty) {

                            Toast.makeText(requireContext(), "message is empty !", Toast.LENGTH_LONG)
                                .show()

                        }

                        else{

                            val documents = value.documents

                            chats.clear()

                            for(document in documents){
                                val text = document.get("chat") as String
                                val user = document.get("user") as String

                                val chat = Chat(text, user)

                                adapter.chat = chats

                                chats.add(chat)


                            }
                        }

                        adapter.notifyDataSetChanged()
                    }


            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}