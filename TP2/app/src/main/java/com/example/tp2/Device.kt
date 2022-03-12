package com.example.tp2

import android.bluetooth.BluetoothClass

data class Device(
    var name: String?,
    var address: String?,
    var deviceClass: BluetoothClass?,
    var type: Int?
)
