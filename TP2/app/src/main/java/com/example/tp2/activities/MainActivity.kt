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
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.fragments.ProfileFragment
import com.example.tp2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
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
    private lateinit var btnShowProfile: Button
    private lateinit var adapter: DeviceAdapter
    private lateinit var recyclerView: RecyclerView
    private var currentPosition: Pair<Double, Double> = Pair(0.0,0.0)
    private var deviceAnnotationsMap: HashMap<PointAnnotation, Device> = HashMap()
    private var markerPositions = HashSet<Pair<Double,Double>>()

    //Dialog attributes
    private lateinit var deviceName: TextView
    private lateinit var deviceAddress: TextView
    private lateinit var deviceClass: TextView
    private lateinit var deviceType: TextView
    private lateinit var shareButton: Button
    private lateinit var navigationButton: Button
    private lateinit var favoriteButton: Button
    private var deviceDialog: Dialog? = null

    //const variables
    private val ISDARKMODEON: String = "isDarkModeOn"
    private val SAVE: String = "save"

    //firebase
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userID: String
    private var databaseService = DatabaseService()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = firebaseUser.uid
        recyclerView = findViewById(R.id.rvDeviceList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DeviceAdapter{ position ->  onRecyclerViewItemClick(position)}
        recyclerView.adapter = adapter
        mapView = findViewById<View>(R.id.mapView) as MapView
        btnSwapTheme = findViewById(R.id.btnSwapTheme)
        btnShowProfile = findViewById(R.id.btnShowProfile)
        btnShowProfile.setOnClickListener {
            displayProfileFragment()
        }
        btnSwapTheme.setOnClickListener {
            swapTheme()
        }
        map = mapView.getMapboxMap()
        setSharedPreferences()
        initDialog()
        setBluetoothAdapter()
        handlePermissions()
    }

    private fun displayProfileFragment(){
        val dialog = ProfileFragment()
        dialog.isCancelable = true
        Handler(Looper.getMainLooper()).postDelayed({ dialog.show(supportFragmentManager, "profile") }, 500)
        //dialog.show(supportFragmentManager, "profile")
    }

    private fun swapTheme() {
        val isDarkModeOn = sharedPreferences.getBoolean(ISDARKMODEON, false)
        if (isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //light mode
            editor.putBoolean(ISDARKMODEON, false)
            editor.commit()
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) //dark mode
            editor.putBoolean(ISDARKMODEON, true)
            editor.commit()
        }
    }

    private fun initDialog() {
        deviceDialog = Dialog(this)
        if (deviceDialog != null) {
            deviceDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            deviceDialog!!.setCancelable(true)
            deviceDialog!!.setContentView(R.layout.fragment_device_info)
            deviceName = deviceDialog!!.findViewById(R.id.tvDeviceName)
            deviceAddress = deviceDialog!!.findViewById(R.id.tvMacAddress)
            deviceClass = deviceDialog!!.findViewById(R.id.tvDeviceClass)
            deviceType = deviceDialog!!.findViewById(R.id.tvDevicetype)
            shareButton = deviceDialog!!.findViewById(R.id.shareButton)
            navigationButton = deviceDialog!!.findViewById(R.id.navigationButton)
            favoriteButton = deviceDialog!!.findViewById(R.id.favoriteButton)
        }
    }

    private fun onRecyclerViewItemClick(position: Int){
        val device = adapter.devices[position]
        showModal(device)
    }
    
    private fun shareLocation(location: Pair<Double, Double>){
        val sendIntent = Intent()
        val geoUri = "http://maps.google.com/maps?q=loc:" + location.first + "," + location.second
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, geoUri)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun addFavorite(device: Device) {
        device.favorite = true
        val favoriteList = ArrayList<Device>()
        for(item in deviceList){
            if (item.favorite){
                favoriteList.add(item)
                adapter.notifyItemChanged(deviceList.indexOf(item))
            }
        }
        val hashMap = HashMap<String, Any>()
        hashMap.put("devices", favoriteList)
        databaseService.update(userID, hashMap)
        for ((key, value) in deviceAnnotationsMap)
        {
            if ( value.address == device.address ) {
                pointAnnotationManager.delete(key)
                deviceAnnotationsMap.remove(key)
                markerPositions.remove(Pair(device.lat, device.lng))
                prepareAnnotationMarker(device)
                break
            }
        }
        //saveListToPreferences(favoriteList)
    }

    private fun initFavorites(){
        deviceList = ArrayList()
        reference.child(userID).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(User::class.java)

                if (userProfile != null){
                    deviceList = userProfile.devices
                    adapter.setDeviceList(deviceList)
                    for(device in deviceList) {
                        prepareAnnotationMarker(device)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Something wrong happened!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showModal(device: Device){
        deviceName.text = device.name
        deviceAddress.text = device.address
        deviceClass.text = getDeviceClass(device.deviceClass)
        deviceType.text = getDeviceType(device.type)
        if (device.favorite){
            favoriteButton.visibility = View.GONE
        }
        else{
            favoriteButton.visibility = View.VISIBLE
        }
        favoriteButton.setOnClickListener{
            addFavorite(device)
            deviceDialog?.dismiss()
        }
        navigationButton.setOnClickListener {
            goToLocation(Pair(device.lat, device.lng))
            deviceDialog?.dismiss()
        }
        shareButton.setOnClickListener {
            shareLocation(Pair(device.lat, device.lng))
        }
        deviceDialog?.show()
    }

    private fun goToLocation(location: Pair<Double, Double>){
        val geoUri = "http://maps.google.com/maps?q=loc:" + location.first + "," + location.second
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(geoUri)
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
        sharedPreferences = getSharedPreferences(SAVE, MODE_PRIVATE)
        editor = sharedPreferences.edit()
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
                    if (device != null && device.name != null) {
                        val formattedDevice = Device(device.name, device.address, device.bluetoothClass.majorDeviceClass, device.type, currentPosition.first, currentPosition.second, false)
                        for(item in deviceList){
                            if(item.address == formattedDevice.address){
                                formattedDevice.favorite = item.favorite
                                for ((key, value) in deviceAnnotationsMap)
                                {
                                    if ( value.address == formattedDevice.address ) {
                                        pointAnnotationManager.delete(key)
                                        deviceAnnotationsMap.remove(key)
                                        markerPositions.remove(Pair(item.lat, item.lng))
                                        prepareAnnotationMarker(formattedDevice)
                                        return
                                    }
                                }
                                return
                            }
                        }
                        addDevice(formattedDevice)
                        prepareAnnotationMarker(formattedDevice)

                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{
                    bluetoothAdapter.startDiscovery()
                }
            }
        }

    }

    private fun addDevice(device: Device){
        val addresses = deviceList.map { it.address }
        if (addresses.contains(device.address)){
            val oldDevice = deviceList.find { it.address == device.address }
            if ( Pair(oldDevice?.lat, oldDevice?.lng) != Pair(device.lat, device.lng)){
                oldDevice?.lat = device.lat
                oldDevice?.lng = device.lng
                return
            }
            return
        }
        deviceList.add(device)
        adapter.notifyItemInserted(deviceList.size)
    }


    private fun onMapReady() {
       map.setCamera(
            CameraOptions.Builder()
                .zoom(18.0)
                .build()
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
        pointAnnotationManager = annotationApi.createPointAnnotationManager()
        pointAnnotationManager.addClickListener(OnPointAnnotationClickListener { annotation ->
            val device = deviceAnnotationsMap[annotation]
            if (device != null) {
                showModal(device)
            }
            true
        })
        initFavorites()
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

    private fun prepareAnnotationMarker(device: Device) {
        if(markerPositions.contains(Pair(device.lat, device.lng))){
            var pair = Pair(device.lat+0.00001, device.lng+0.00001)
            while(markerPositions.contains(pair)){
                pair = Pair(pair.first+0.00001 , pair.second+0.00001)
            }
            device.lat = pair.first
            device.lng = pair.second
        }
        markerPositions.add(Pair(device.lat, device.lng))
        var drawable = R.drawable.blue_marker
        if(device.favorite){
            drawable = R.drawable.red_marker
        }
        val bitmap = convertDrawableToBitmap(drawable)
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(device.lng, device.lat))
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
        adapter.clear()
        super.onDestroy()
    }
}