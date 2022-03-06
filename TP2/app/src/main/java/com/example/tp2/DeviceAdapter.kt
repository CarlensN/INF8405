package com.example.tp2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.maps.extension.style.expressions.dsl.generated.switchCase

class DeviceAdapter(var devices : List<BluetoothDevice>) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {


    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvDeviceName : TextView = itemView.findViewById(R.id.tvDeviceName)
        val tvMacAddress : TextView = itemView.findViewById(R.id.tvMacAddress)
        val tvDeviceType : TextView = itemView.findViewById(R.id.tvDevicetype)
        val tvDeviceClass : TextView = itemView.findViewById(R.id.tvDeviceClass)
        val tvDeviceState : TextView = itemView.findViewById(R.id.tvDeviceBondState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.tvDeviceName.text = devices[position].name
        holder.tvMacAddress.text = devices[position].address
        holder.tvDeviceType.text = getDeviceType(devices[position].type)
        holder.tvDeviceClass.text = devices[position].bluetoothClass.deviceClass.toString()
        holder.tvDeviceState.text = devices[position].bondState.toString()
    }

    private fun getDeviceType(type: Int): String {
        return when(type){
            1 -> "Classic"
            2 -> "Dual Mode"
            3 -> "Low Energy"
            4 -> "Unknown"
            else -> {"N/A"}
        }
    }

    private fun getDeviceClass(className: Int): String {
        return when(className){
            1 -> "Classic BR/EDR"
            2 -> "Dual Mode - BR/EDR/LE"
            3 -> "Low Energy - LE-only"
            4 -> "Unknown"
            else -> {"N/A"}
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}