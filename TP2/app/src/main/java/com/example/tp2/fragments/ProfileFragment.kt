package com.example.tp2.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tp2.AccountActivity
import com.example.tp2.MainActivity
import com.example.tp2.R
import com.example.tp2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

class ProfileFragment : DialogFragment() {
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userID: String
    private lateinit var logout: Button
    private lateinit var btnMain: Button
    private lateinit var tvUsername: TextView
    private lateinit var profilePicture: ImageView
    private lateinit var swapThemeBtn: Button
    private lateinit var btnChangeLanguage: Button

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout = view.findViewById(R.id.btnLogout)
        btnMain = view.findViewById(R.id.btnGoToMain)
        tvUsername = view.findViewById(R.id.tvUsername)
        profilePicture = view.findViewById(R.id.profilePicture)
        swapThemeBtn = view.findViewById(R.id.btnSwapTheme)
        btnChangeLanguage = view.findViewById(R.id.btnChangeLanguage)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, AccountActivity::class.java))
            activity?.finish()
        }

        btnMain.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        btnChangeLanguage.setOnClickListener{
            this.changeLanguage()
        }

        swapThemeBtn.setOnClickListener{
            this.swapThemes()
        }

        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user.uid

        reference.child(userID).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(User::class.java)

                if (userProfile != null){
                    tvUsername.text = userProfile.username
                    val bitmap = decodeFromFirebaseBase64(userProfile.imageEncoded)
                    profilePicture.setImageBitmap(bitmap)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something wrong happened!", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun swapThemes(){
        (this.activity as MainActivity).swapTheme()
    }

    fun changeLanguage(){
        if(btnChangeLanguage.text == "FR")
            (this.activity as MainActivity).setLocale("fr")
        else
            (this.activity as MainActivity).setLocale("")
    }

    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }


}