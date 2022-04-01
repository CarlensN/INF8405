package com.example.tp2

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tp2.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream


class RegisterFragment : Fragment() {
    val emailTemplate = "@template.com"
    private var mAuth: FirebaseAuth? = null
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var btnCamera: Button
    private lateinit var btnRegister: Button
    private lateinit var userPicture: ImageView
    private var imageEncoded: String = ""

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var databaseService = DatabaseService()
        mAuth = FirebaseAuth.getInstance()
        username = view.findViewById(R.id.username_editText)
        password = view.findViewById(R.id.password_editText)
        btnCamera = view.findViewById(R.id.btnPicture)
        btnRegister = view.findViewById(R.id.btnRegister)
        userPicture = view.findViewById(R.id.userPicture)
        btnCamera.setOnClickListener {
            if (this.context?.let { it1 -> ContextCompat.checkSelfPermission(it1, android.Manifest.permission.CAMERA) } == PackageManager.PERMISSION_GRANTED){
                startCameraIntent()
            }
            else{
                ActivityCompat.requestPermissions(this.context as Activity, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        }
        btnRegister.setOnClickListener {
            /*val user = User(username.text.toString(), password.text.toString(), imageEncoded)
            databaseService.add(user).addOnSuccessListener {
                Toast.makeText(this.context, "Record is inserted", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this.context, "Couldn't insert record", Toast.LENGTH_LONG).show()
            }*/
            registerUser()
        }
    }

    private fun registerUser() {
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

        if(imageEncoded.isEmpty()){
            btnCamera.error = "Profile picture is required"
            btnCamera.requestFocus()
            return
        }

        val email = usernameString + emailTemplate

        mAuth?.createUserWithEmailAndPassword(email, passwordString)
            ?.addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    val user = User(usernameString, email, imageEncoded)

                    FirebaseAuth.getInstance().currentUser?.let { it1 ->
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it1.uid)
                            .setValue(user).addOnCompleteListener { task2 ->
                                if(task2.isSuccessful){
                                    Toast.makeText(this.context, "User has been registered successfully!", Toast.LENGTH_LONG).show()

                                    //redirect to main activity
                                }
                                else{
                                    Toast.makeText(this.context, "Failed to register, try again.", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                }
                else{
                    Toast.makeText(this.context, "Failed to register, try again.", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun startCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCameraIntent()
            }
            else{
                Toast.makeText(this.context, "Permission was denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST_CODE && data != null){
                val bitmap: Bitmap = data.extras!!.get("data") as Bitmap
                userPicture.setImageBitmap(bitmap)
                Toast.makeText(this.context, "picture added", Toast.LENGTH_LONG).show()
                userPicture.visibility = View.VISIBLE
                imageEncoded = encodeBitmapAndSaveToFirebase(bitmap)
            }
        }

    }

    fun encodeBitmapAndSaveToFirebase(bitmap: Bitmap): String {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        return Base64.encodeToString(byteArray.toByteArray(), Base64.DEFAULT)
    }
}