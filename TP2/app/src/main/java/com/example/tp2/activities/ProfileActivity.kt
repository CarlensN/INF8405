package com.example.tp2.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp2.AccountActivity
import com.example.tp2.MainActivity
import com.example.tp2.R
import com.example.tp2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ProfileActivity : AppCompatActivity() {
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userID: String
    private lateinit var logout: Button
    private lateinit var btnMain: Button
    private lateinit var tvUsername: TextView
    private lateinit var profilePicture: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        logout = findViewById(R.id.btnLogout)
        btnMain = findViewById(R.id.btnGoToMain)
        tvUsername = findViewById(R.id.tvUsername)
        profilePicture = findViewById(R.id.profilePicture)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
        }

        btnMain.setOnClickListener {
            if (!MainActivity.active){
                finish()
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user.uid

        reference.child(userID).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(User::class.java)

                if (userProfile != null){
                    tvUsername.text = userProfile.username
                    val bitmap = decodeFromFirebaseBase64(userProfile.imageEncoded)
                    profilePicture.setImageBitmap(bitmap)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Something wrong happened!", Toast.LENGTH_LONG).show()
            }

        })

    }

    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }
}