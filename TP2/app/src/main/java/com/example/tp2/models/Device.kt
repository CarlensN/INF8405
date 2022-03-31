package com.example.tp2

data class Device(
    var name: String?,
    var address: String?,
    var deviceClass: Int?,
    var type: Int?,
    var location: Pair<Double, Double>,
    var favorite: Boolean
)
