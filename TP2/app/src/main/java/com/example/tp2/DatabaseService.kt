package com.example.tp2

import com.example.tp2.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseService {
    private var databaseReference: DatabaseReference

    constructor(){
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference(User::class.java.simpleName)
    }

    fun add(user: User) : Task<Void>{
        return databaseReference.push().setValue(user)
    }
}