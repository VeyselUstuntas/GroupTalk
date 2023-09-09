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
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vustuntas.grouptalk.R
import com.vustuntas.grouptalk.activity.ChatScreen
import com.vustuntas.grouptalk.databinding.FragmentSignInBinding

class SignIn : Fragment() {
    private lateinit var _binding : FragmentSignInBinding
    private val binding get() = _binding.root
    private lateinit var auth :FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.signInButton.setOnClickListener { signIn(it) }
        _binding.signUpButton.setOnClickListener { signUp(it) }
        _binding.signInShowPasswordCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if(b)
                _binding.signInPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()

            else
                _binding.signInPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        if(auth.currentUser != null){
            activity?.let{
                val intent = Intent(it,ChatScreen::class.java)
                startActivity(intent)
                it.finish()
            }
        }
    }

    private fun signIn(view:View){
        val userMailAddress = _binding.signInMailAddressEditText.text.toString()
        val userPassword = _binding.signInPasswordEditText.text.toString()
        if(!userMailAddress.equals("") && !userPassword.equals("")){
            auth.signInWithEmailAndPassword(userMailAddress,userPassword)
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
        else
            Toast.makeText(context,"Enter Mail address and Pasword",Toast.LENGTH_LONG).show()
    }

    private fun signUp(view:View){
        val action = SignInDirections.actionSignInToSignUp()
        Navigation.findNavController(view).navigate(action)
    }
}