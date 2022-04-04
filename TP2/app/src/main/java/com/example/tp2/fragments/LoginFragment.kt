package com.example.tp2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tp2.fragments.ProfileFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    val emailTemplate = "@template.com"
    private var mAuth: FirebaseAuth? = null
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        username = view.findViewById(R.id.username_editText)
        password = view.findViewById(R.id.password_editText)
        btnLogin = view.findViewById(R.id.login_button)

        btnLogin.setOnClickListener {
            logUser()
        }
    }

    private fun logUser() {
        val usernameString = username.text.toString().trim()
        val passwordString = password.text.toString().trim()

        if(usernameString.isEmpty()){
            username.error = "Username is required!"
            username.requestFocus()
            return
        }

        if(passwordString.isEmpty()){
            password.error = "Password is required!"
            password.requestFocus()
            return
        }

        if (passwordString.length < 6){
            password.error = "Min password length should be 6 characters!"
            password.requestFocus()
            return
        }

        val email = usernameString + emailTemplate;

        mAuth?.signInWithEmailAndPassword(email, passwordString)?.addOnCompleteListener {
            if (it.isSuccessful){
                // redirect to main activity
                startActivity(Intent(this.context, MainActivity::class.java))
                activity?.finish()
            }
            else{
                Toast.makeText(this.context, "Failed to login, please check your credentials.", Toast.LENGTH_LONG).show()
            }
        }
    }
}