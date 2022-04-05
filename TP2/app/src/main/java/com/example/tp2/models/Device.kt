package com.example.tp2

import com.example.tp2.models.CustomPair

class Device{
    lateinit var name: String
    lateinit var address: String
    var deviceClass: Int = 0
    var type: Int = 0
    lateinit var location: CustomPair
    var favorite: Boolean = false

    constructor(){}

    constructor(name: String, address: String, deviceClass: Int, type: Int, location: CustomPair, favorite: Boolean){
        this.name = name
        this.address = address
        this.deviceClass = deviceClass
        this.type = type
        this.location = location
        this.favorite = favorite
    }
}
