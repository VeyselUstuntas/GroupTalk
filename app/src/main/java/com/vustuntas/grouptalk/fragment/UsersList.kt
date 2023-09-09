package com.vustuntas.grouptalk.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.arraySetOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vustuntas.grouptalk.R
import com.vustuntas.grouptalk.adapter.UsersAdapter
import com.vustuntas.grouptalk.databinding.FragmentMessagesBinding
import com.vustuntas.grouptalk.databinding.FragmentUsersListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.FileInputStream
import java.util.ArrayList

class UsersList : Fragment() {
    private lateinit var _binding : FragmentUsersListBinding
    private val binding get() = _binding.root
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var adapter : UsersAdapter
    private lateinit var usersArraylist : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersListBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        firestore = Firebase.firestore
        usersArraylist = ArrayList<String>()
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking (Dispatchers.IO){
            launch {
                coroutineScope {
                    getUsers()
                }
            }
        }
        adapter = UsersAdapter(usersArraylist)
        _binding.recyclerView.layoutManager = LinearLayoutManager(context)
        _binding.recyclerView.adapter = adapter

        println(usersArraylist.size)

    }

    private suspend fun getUsers(){
        if(auth.currentUser != null){
            val querySnapshot = firestore.collection("Users").get().await()
            usersArraylist.clear()
            for(i in querySnapshot.documents){
                val userMailAddress = i.get("userMail") as String
                println(userMailAddress)
                usersArraylist.add(userMailAddress)
            }
        }
    }

}