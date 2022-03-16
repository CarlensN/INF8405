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

class DeviceAdapter(private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    var devices: ArrayList<Device> = ArrayList()


    inner class DeviceViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val tvDeviceName : TextView = itemView.findViewById(R.id.tvDeviceName)
        val tvMacAddress : TextView = itemView.findViewById(R.id.tvMacAddress)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view, onItemClicked)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.tvDeviceName.text = devices[position].name
        holder.tvMacAddress.text = devices[position].address
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    @JvmName("setDeviceList1")
    public fun setDeviceList(list : ArrayList<Device>){
        devices = list
        notifyDataSetChanged()
    }

    fun clear(){
        devices.clear()
        notifyDataSetChanged()
    }

    public fun addDevice(device:Device){

    }
}