package com.example.tp2.models

import android.media.Image

data class UserRegistration(var username: String, var password: String, var avatar: Image)


data class UserLogin(var username: String, var password: String)