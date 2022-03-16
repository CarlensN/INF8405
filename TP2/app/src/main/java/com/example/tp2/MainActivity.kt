package com.example.tp2

import android.annotation.SuppressLint
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.reflect.Type
import kotlin.math.abs


class MainActivity : AppCompatActivity(), PermissionsListener{
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var pointAnnotationManager:PointAnnotationManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var deviceList: ArrayList<Device>
    private lateinit var btnSwapTheme: Button
    private lateinit var adapter: DeviceAdapter
    private lateinit var recyclerView: RecyclerView
    private var currentPosition: Pair<Double, Double> = Pair(0.0,0.0)
    private var deviceAnnotationsMap: HashMap<PointAnnotation,Device> = HashMap()

    //Dialog attributes
    private lateinit var deviceName: TextView
    private lateinit var deviceAddress: TextView
    private lateinit var deviceClass: TextView
    private lateinit var deviceType: TextView
    private lateinit var shareButton: Button
    private lateinit var navigationButton: Button
    private lateinit var favoriteButton: Button
    private var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rvDeviceList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DeviceAdapter{ position ->  onRecyclerViewItemClick(position)}
        recyclerView.adapter = adapter
        mapView = findViewById<View>(R.id.mapView) as MapView
        btnSwapTheme = findViewById(R.id.btnSwapTheme)
        map = mapView.getMapboxMap()
        setSharedPreferences()
        initFavorites()
        initDialog()
        setBluetoothAdapter()
        handlePermissions()
    }

    private fun initDialog() {
        dialog = Dialog(this)
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
        Toast.makeText(this, "salut", Toast.LENGTH_SHORT).show()
        val device = adapter.devices[position]
        showModal(device)
        favoriteButton.setOnClickListener{
            addFavorite(adapter.devices[position])
            dialog?.dismiss()
        }
        navigationButton.setOnClickListener {
            goToLocation(adapter.devices[position].location)
            dialog?.dismiss()
        }
    }

    private fun addFavorite(device: Device) {
        device.favorite = true
        val favoriteList = ArrayList<Device>()
        for(item in deviceList){
            if (item.favorite){
                favoriteList.add(item)
            }
        }
        saveListToPreferences("favorites", favoriteList)
    }

    private fun initFavorites(){
        deviceList = getListFromPreferences("favorites")
        adapter.setDeviceList(deviceList)
    }

    private fun showModal(device: Device){
        deviceName.text = device.name
        deviceAddress.text = device.address
        deviceClass.text = device.deviceClass?.let { getDeviceClass(it) }
        deviceType.text = device.type?.let { getDeviceType(it) }
        if (device.favorite){
            favoriteButton.visibility = View.GONE
        }
        else{
            favoriteButton.visibility = View.VISIBLE
        }
        dialog?.show()
    }

    private fun goToLocation(location: Pair<Double, Double>){
        val url = "https://maps.google.com/?q=<${location.first}>,<${location.second}>"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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

    private fun setSharedPreferences(){
        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun saveListToPreferences(key: String, list: List<Device>){
        val gson = Gson()
        val json : String = gson.toJson(list)
        set(key, json)
    }

    operator fun set(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    private fun getListFromPreferences(key: String): ArrayList<Device> {
        var arrayItems: ArrayList<Device> = ArrayList()
        val serializedObject = sharedPreferences.getString(key, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Device>?>() {}.type
            arrayItems = gson.fromJson(serializedObject, type)
        }
        return arrayItems
    }


    private fun handlePermissions(){
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }

        discoverDevices()
    }

    private fun setBluetoothAdapter(){
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    @SuppressLint("MissingPermission")
    private fun discoverDevices(){
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(receiver, filter)
        bluetoothAdapter.startDiscovery()
    }


    private val receiver = object : BroadcastReceiver(){
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent?) {
            when(intent?.action){
                BluetoothDevice.ACTION_FOUND ->{
                    val device : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("device", "${device?.name} + ${device?.address}")
                    if (device != null && device.name != null) {
                        val formattedDevice = Device(device.name, device.address, device.bluetoothClass.majorDeviceClass, device.type, currentPosition, false)
                        //deviceFragment.addDevice(formattedDevice)
                        if (!deviceList.contains(formattedDevice)){
                            addDevice(formattedDevice)
                            prepareAnnotationMarker(formattedDevice, Point.fromLngLat(currentPosition.second, currentPosition.first))
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Toast.makeText(context, "Starting device discovery", Toast.LENGTH_SHORT).show()
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{
                    Toast.makeText(context, "Scanning Done", Toast.LENGTH_SHORT).show()
                    bluetoothAdapter.startDiscovery()
                }
            }
        }

    }

    private fun addDevice(device: Device){
        deviceList.add(device)
        adapter.notifyDataSetChanged()
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
            if (abs(it.latitude() - currentPosition.first) >= 0.05 || abs(it.longitude() - currentPosition.second) >= 0.05){
                currentPosition = Pair(it.latitude(), it.longitude())
            }
        }
        mapView.location
            .addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)

        val annotationApi = mapView.annotations
        pointAnnotationManager = annotationApi.createPointAnnotationManager(mapView)
        pointAnnotationManager.addClickListener(object: OnPointAnnotationClickListener{
            override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                val device = deviceAnnotationsMap[annotation]
                if (device != null) {
                    /*if (favoriteDevices.contains(device)) {
                        favoriteFragment.showModal(device)
                    }else {
                        deviceFragment.showModal(device)
                    }*/
                }
                return true
            }
        })
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

    private fun prepareAnnotationMarker(device:Device, point: Point) {
        val bitmap = convertDrawableToBitmap(R.drawable.red_marker)
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap!!)
            .withIconAnchor(IconAnchor.BOTTOM)
            .withIconSize(0.5)
        val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
        deviceAnnotationsMap[pointAnnotation] = device
    }

    private fun convertDrawableToBitmap(id: Int): Bitmap? {
        val sourceDrawable = AppCompatResources.getDrawable(this,id) ?: return null
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
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
           onMapReady()
       }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroy() {
        unregisterReceiver(receiver)
        pointAnnotationManager.deleteAll()
        super.onDestroy()
    }
}