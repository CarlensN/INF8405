package com.example.tp2

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.geojson.Point

class DeviceFragment() : Fragment() {
    private lateinit var adapter: DeviceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var deviceName: TextView
    private lateinit var deviceAddress: TextView
    private lateinit var deviceClass: TextView
    private lateinit var deviceType: TextView
    private lateinit var shareButton: Button
    private lateinit var navigationButton: Button
    private lateinit var favoriteButton: Button
    private var dialog:Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvDeviceList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DeviceAdapter{ position ->  onRecyclerViewItemClick(position)}
        recyclerView.adapter = adapter
        dialog = this.context?.let { Dialog(it) }
        if (dialog != null) {
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(true)
            dialog!!.setContentView(R.layout.fragment_device_info)
            deviceName = dialog!!.findViewById(R.id.tvDeviceName)
            deviceAddress = dialog!!.findViewById(R.id.tvMacAddress)
            deviceClass = dialog!!.findViewById(R.id.tvDeviceClass)
            deviceType = dialog!!.findViewById(R.id.tvDevicetype)
            shareButton = dialog!!.findViewById(R.id.shareButton)
            navigationButton = dialog!!.findViewById(R.id.navigationButton)
            favoriteButton = dialog!!.findViewById(R.id.favoriteButton)
        }
    }

    @SuppressLint("MissingPermission")
    private fun onRecyclerViewItemClick(position: Int){
        Toast.makeText(this.context, adapter.devices[position].location.toString(), Toast.LENGTH_SHORT).show()

        val device = adapter.devices[position]
        showModal(device)
        favoriteButton.setOnClickListener{
            saveFavorite(adapter.devices[position])
            dialog?.dismiss()
        }
        navigationButton.setOnClickListener {
            goToLocation(adapter.devices[position].location)
            dialog?.dismiss()
        }
    }
    fun showModal(device: Device){
        deviceName.text = device.name
        deviceAddress.text = device.address
        deviceClass.text = device.deviceClass?.let { getDeviceClass(it) }
        deviceType.text = device.type?.let { getDeviceType(it) }
        dialog?.show()
    }

    fun goToLocation(location: Pair<Double, Double>){
        val url = "https://maps.google.com/?q=<${location.first}>,<${location.second}>"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun saveFavorite(device: Device){
        val myActivity: MainActivity = this.activity as MainActivity
        myActivity.addFavorite(device)
    }

    private fun getDeviceType(type: Int): String {
        return when(type){
            1 -> "Classic"
            2 -> "Dual Mode"
            3 -> "Low Energy"
            else -> {"Unknown"}
        }
    }

    private fun getDeviceClass(className: Int): String {
        return when(className){
            1024 -> "AUDIO_VIDEO"
            256 -> "COMPUTER"
            2304 -> "HEALTH"
            512 -> "PHONE"
            1792 -> "WEARABLE"
            1280 -> "PERIPHERAL"
            768 -> "NETWORKING"
            else -> {"UNCATEGORIZED"}
        }
    }

    fun getAdapter() : DeviceAdapter{
        return adapter
    }

    fun getDevices() : ArrayList<Device>{
        return adapter.devices
    }

    fun getRecyclerView() : RecyclerView{
        return recyclerView
    }

    fun addDevice(device: Device){
        adapter.devices.add(adapter.itemCount, device)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    fun addDevices(list: List<Device>){
        adapter.clear()
        adapter.devices.addAll(list)
        adapter.notifyDataSetChanged()
    }
}

