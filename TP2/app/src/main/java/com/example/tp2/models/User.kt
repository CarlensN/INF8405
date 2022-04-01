package com.example.tp2.models

import android.net.Uri

class User {
    lateinit var username: String
    lateinit var email: String
    lateinit var imageEncoded: String

    constructor(){}

    constructor(username: String, email: String, imageEncoded: String) {
        this.username = username
        this.email = email
        this.imageEncoded = imageEncoded
    }
}