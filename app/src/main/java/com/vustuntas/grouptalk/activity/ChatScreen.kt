package com.vustuntas.grouptalk.activity

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vustuntas.grouptalk.R
import com.vustuntas.grouptalk.databinding.ActivityChatScreenBinding
import com.vustuntas.grouptalk.fragment.MessagesDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.ArrayList
import java.util.HashMap

class ChatScreen : AppCompatActivity() {
    private lateinit var binding: ActivityChatScreenBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var users : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        users = ArrayList<String>()
        auth = Firebase.auth
        firestore = Firebase.firestore
        saveUserID()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toolbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.accountLogout){
            auth.signOut()
            val intent = Intent(this@ChatScreen,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(item.itemId == R.id.toolbar_userList){
            val action = MessagesDirections.actionMessagesToUsersList()
            Navigation.findNavController(this@ChatScreen,R.id.fragmentContainerView2).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveUserID() {
        if (auth.currentUser != null) {
            val userId = auth.currentUser!!.uid
            GlobalScope.launch(Dispatchers.IO) {
                val available = userAvailable(userId)
                if (!available) {
                    val userIdHashMap = HashMap<String,Any>()
                    userIdHashMap.put("userId",userId)
                    userIdHashMap.put("userMail",auth.currentUser!!.email!!)
                    try {
                        firestore.collection("Users").add(userIdHashMap).await()
                    } catch (e: Exception) {
                        Toast.makeText(this@ChatScreen, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private suspend fun userAvailable(userID: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("Users").get().await()
            for (document in querySnapshot.documents) {
                val fsUserId = document.getString("userId") as String
                if (fsUserId == userID) {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            Toast.makeText(this@ChatScreen, e.localizedMessage, Toast.LENGTH_LONG).show()
            false
        }
    }

}