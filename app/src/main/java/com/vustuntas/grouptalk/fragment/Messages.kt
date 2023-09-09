package com.vustuntas.grouptalk.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vustuntas.grouptalk.R
import com.vustuntas.grouptalk.adapter.MessageAdapter
import com.vustuntas.grouptalk.databinding.FragmentMessagesBinding
import com.vustuntas.grouptalk.helperClass.MessageInfo
import java.util.ArrayList
import java.util.HashMap

class Messages : Fragment() {
    private lateinit var _binding : FragmentMessagesBinding
    private val binding get() = _binding.root
    private lateinit var adapter : MessageAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var messageInfoArraylist : ArrayList<MessageInfo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater,container,false)
        _binding.sendMessageButton.setOnClickListener { sendMessage(it) }
        auth = Firebase.auth
        firestore = Firebase.firestore
        messageInfoArraylist = ArrayList<MessageInfo>()

        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMessages()
        adapter = MessageAdapter(messageInfoArraylist)
        val layoutManager = LinearLayoutManager(context)
        _binding.chatMessageRecyclerView.layoutManager = layoutManager
        _binding.chatMessageRecyclerView.adapter = adapter
        layoutManager.stackFromEnd = true // açılışta son mesaja gider
    }


    private fun sendMessage(view: View){
        val userMessage : String = _binding.enterMessageEditText.text.toString()
        if(auth.currentUser != null){
            if(!userMessage.equals("")){
                val messageMap = HashMap<String,Any>()
                messageMap.put("userMailAddress",auth.currentUser!!.email!!)
                messageMap.put("userMessage",userMessage)
                messageMap.put("time", Timestamp.now())

                firestore.collection("UsersMessage").add(messageMap)
                    .addOnSuccessListener {
                        _binding.enterMessageEditText.text.clear()
                        val lastIndex = adapter.itemCount-1
                        _binding.chatMessageRecyclerView.smoothScrollToPosition(lastIndex)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun getMessages(){
        if(auth.currentUser != null){
            firestore.collection("UsersMessage").orderBy("time",
                Query.Direction.ASCENDING).addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(context,error.localizedMessage, Toast.LENGTH_LONG).show()
                }
                else{
                    if(value != null){
                        if(!value.isEmpty ){
                            val messages = value.documents
                            messageInfoArraylist.clear()
                            for(i in messages){
                                val userMailAddress = i.get("userMailAddress") as String
                                val userMessage = i.get("userMessage") as String

                                val messageInfoObject = MessageInfo(userMailAddress, userMessage)
                                messageInfoArraylist.add(messageInfoObject)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }


}