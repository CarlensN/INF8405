package com.example.tp2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
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
    private lateinit var discoveredDevices: ArrayList<Device>
    private lateinit var favoriteDevices: ArrayList<Device>
    private lateinit var newDevices: ArrayList<Device>
    private lateinit var btnSwapTheme: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var deviceFragment: DeviceFragment
    private lateinit var favoriteFragment: FavoriteFragment
    private var currentPosition: Pair<Double, Double> = Pair(0.0,0.0)
    private var deviceAnnotationsMap: HashMap<PointAnnotation,Device> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        mapView = findViewById<View>(R.id.mapView) as MapView
        btnSwapTheme = findViewById(R.id.btnSwapTheme)
        map = mapView.getMapboxMap()
        setSharedPreferences()
        discoveredDevices = ArrayList()
        favoriteDevices = getListFromPreferences("favorites")
        newDevices = ArrayList()
        setBluetoothAdapter()
        setUpFragments()
        setUpTabs()
        handlePermissions()
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

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(deviceFragment, "Devices")
        adapter.addFragment(favoriteFragment, "Favorites")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setUpFragments(){
        deviceFragment = DeviceFragment()
        favoriteFragment = FavoriteFragment()
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
                        val formattedDevice = Device(device.name, device.address, device.bluetoothClass.majorDeviceClass, device.type, currentPosition)
                        //deviceFragment.addDevice(formattedDevice)
                        newDevices.add(formattedDevice)
                        //favoriteFragment.addDevice(device)
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Toast.makeText(context, "Starting device discovery", Toast.LENGTH_SHORT).show()
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{
                    addNewDevices(newDevices)
                    newDevices.clear()
                    Toast.makeText(context, "Scanning Done", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun addFavorite(device: Device){
        if (!favoriteDevices.contains(device)){
            favoriteDevices.add(device)
            favoriteFragment.addDevice(device)
        }
        saveListToPreferences("favorites", favoriteDevices)
    }

    private fun displayDevices(){
        deviceFragment.addDevices(discoveredDevices)
        favoriteFragment.addDevices(favoriteDevices)
        for(item in discoveredDevices){
            val bitmap = convertDrawableToBitmap(R.drawable.red_marker)
            if (bitmap != null) {
                prepareAnnotationMarker(item, mapView, bitmap, Point.fromLngLat(item.location.second, item.location.first))
            }
        }
    }

     fun displayFavorites(){
        favoriteFragment.addDevices(favoriteDevices)
        for(item in favoriteDevices){
            val bitmap = convertDrawableToBitmap(R.drawable.red_marker)
            if (bitmap != null) {
                prepareAnnotationMarker(mapView, bitmap, Point.fromLngLat(item.location.second, item.location.first))
            }
        }
    }

    private fun addNewDevices(list: List<Device>){
        val knownAddresses = discoveredDevices.map { it.address }
        for(item in list){
            if (knownAddresses.contains(item.address)){
                discoveredDevices.removeAll{it.address == item.address}
            }
            discoveredDevices.add(item)
        }
        displayDevices()
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
                Toast.makeText(this@MainActivity, "hallo", Toast.LENGTH_SHORT).show()
                deviceAnnotationsMap[annotation]?.let { deviceFragment.showModal(it) }
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

    private fun prepareAnnotationMarker(device:Device, mapview: MapView, iconBitmap: Bitmap, point: Point) {
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(iconBitmap)
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