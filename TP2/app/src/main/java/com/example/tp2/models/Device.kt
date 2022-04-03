package com.example.tp2

class Device{
    lateinit var name: String
    lateinit var address: String
    var deviceClass: Int = 0
    var type: Int = 0
    var lat: Double = 0.0
    var lng: Double = 0.0
    var favorite: Boolean = false

    constructor(){}

    constructor(name: String, address: String, deviceClass: Int, type: Int, lat: Double, lng: Double, favorite: Boolean){
        this.name = name
        this.address = address
        this.deviceClass = deviceClass
        this.type = type
        this.lat = lat
        this.lng = lng
        this.favorite = favorite
    }
}
