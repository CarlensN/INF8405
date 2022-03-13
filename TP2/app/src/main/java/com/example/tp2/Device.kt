package com.example.tp2

import com.mapbox.geojson.Point

data class Device(
    var name: String?,
    var address: String?,
    var deviceClass: Int?,
    var type: Int?,
    var location: Pair<Double, Double>
)
