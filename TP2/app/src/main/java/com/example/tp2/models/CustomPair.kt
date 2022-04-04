package com.example.tp2.models

import kotlin.properties.Delegates

class CustomPair {
    var first: Double = 0.0
    var second: Double = 0.0

    constructor()

    constructor(first: Double, second: Double){
        this.first = first
        this.second = second
    }
}