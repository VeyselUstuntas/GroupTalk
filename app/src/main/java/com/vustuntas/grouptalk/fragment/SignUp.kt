package com.vustuntas.grouptalk.fragment

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vustuntas.grouptalk.R
import com.vustuntas.grouptalk.activity.ChatScreen
import com.vustuntas.grouptalk.databinding.FragmentSignInBinding
import com.vustuntas.grouptalk.databinding.FragmentSignUpBinding

class SignUp : Fragment() {
    private lateinit var _binding : FragmentSignUpBinding
    private val binding get() = _binding.root
    private lateinit var  auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.signUpButton.setOnClickListener { signUp(it) }
        _binding.signUpShowPasswordCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                _binding.signUpPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                _binding.signUpPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

    }
    private fun signUp(view:View){
        val userMailAddress = _binding.signUpMailAddressEditText.text.toString()
        val userPassword = _binding.signUpPasswordEditText.text.toString()

        if(!userMailAddress.equals("") && !userPassword.equals("")){
            auth.createUserWithEmailAndPassword(userMailAddress,userPassword)
                .addOnSuccessListener {
                    activity?.let {
                        val intent = Intent(it,ChatScreen::class.java)
                        startActivity(intent)
                        it.finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
        else{
            Toast.makeText(context,"Please enter user mail and user password",Toast.LENGTH_LONG).show()
        }

    }
}