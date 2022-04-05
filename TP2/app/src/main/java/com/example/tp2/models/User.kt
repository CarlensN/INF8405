package com.example.tp2.models

import com.example.tp2.Device

class User {
    lateinit var username: String
    lateinit var email: String
    lateinit var imageEncoded: String
    var devices: ArrayList<Device> = ArrayList()

    constructor(){}

    constructor(username: String, email: String, imageEncoded: String) {
        this.username = username
        this.email = email
        this.imageEncoded = imageEncoded
    }
}