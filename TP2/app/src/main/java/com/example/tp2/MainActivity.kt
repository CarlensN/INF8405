package com.example.tp2

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), PermissionsListener {
    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var deviceList: ArrayList<BluetoothDevice>
    private lateinit var btnSwapTheme: Button

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        mapView = findViewById<View>(R.id.mapView) as MapView
        btnSwapTheme = findViewById(R.id.btnSwapTheme)
        map = mapView.getMapboxMap()
        deviceList = ArrayList()
        deviceAdapter = DeviceAdapter(deviceList)
        val deviceRecyclerView = findViewById<RecyclerView>(R.id.rvDeviceListeviceList)
        deviceRecyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        deviceRecyclerView.adapter = deviceAdapter
        handlePermissions()
    }



    @RequiresApi(Build.VERSION_CODES.R)
    private fun handlePermissions(){
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
        else{
            discoverDevices()
        }
    }


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getPairedDevices(){
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()){
            val list: List<BluetoothDevice> = pairedDevices.toList()
            deviceAdapter = DeviceAdapter(list)
            val deviceRecyclerView = findViewById<RecyclerView>(R.id.rvDeviceListeviceList)
            deviceRecyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
            deviceRecyclerView.adapter = deviceAdapter
        }

        /*pairedDevices.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            val alias = device.alias
            val type = device.type
            val bclass = device.bluetoothClass
            val bondstate = device.bondState

            Log.d("device", "$deviceName ---- $deviceHardwareAddress")
        }*/
    }

    private fun discoverDevices(){
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(receiver, filter)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothAdapter.startDiscovery()
    }

    private val receiver = object : BroadcastReceiver(){
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent?) {
            val action : String? = intent?.action
            when(action){
                BluetoothDevice.ACTION_FOUND ->{
                    val device : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("device", "${device?.name} + ${device?.address}")
                    if (device != null) {
                        deviceList.add(deviceAdapter.itemCount, device)
                        deviceAdapter.notifyItemInserted(deviceAdapter.itemCount)
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            //getPairedDevices()
            discoverDevices()
        }else{
            return@registerForActivityResult
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

    private fun onMapReady() {
       map.setCamera(
            CameraOptions.Builder()
                .zoom(18.0)
                .build()
       )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        )
        initLocationComponent()
        val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
            // Jump to the current indicator position
            mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        }
        mapView.location
            .addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location

        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                topImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    com.mapbox.maps.plugin.locationcomponent.R.drawable.mapbox_user_icon
                ),
                bearingImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    com.mapbox.maps.plugin.locationcomponent.R.drawable.mapbox_user_bearing_icon
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    com.mapbox.maps.plugin.locationcomponent.R.drawable.mapbox_user_stroke_icon
                ),
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(p0: MutableList<String>?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionResult(p0: Boolean) {
       if(p0){
           onMapReady();
       }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroy() {
        unregisterReceiver(receiver)
        deviceList.clear()
        deviceAdapter.notifyDataSetChanged()
        super.onDestroy()
    }
}