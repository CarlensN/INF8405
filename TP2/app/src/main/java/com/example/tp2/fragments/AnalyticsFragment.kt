package com.example.tp2.fragments

import android.app.Dialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.tp2.R


class AnalyticsFragment : DialogFragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private var magneticFieldSensor: Sensor? = null
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var nc: NetworkCapabilities

    private lateinit var magneticField: TextView
    private lateinit var pressure: TextView
    private lateinit var battery: TextView
    private lateinit var uplink: TextView
    private lateinit var downlink: TextView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager = activity?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) as NetworkCapabilities
        sensorManager =  activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_analytics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        magneticField = view.findViewById(R.id.tvMagneticField)
        pressure = view.findViewById(R.id.tvPressure)
        battery = view.findViewById(R.id.tvBattery)
        uplink = view.findViewById(R.id.tvUplink)
        downlink = view.findViewById(R.id.tvDownlink)
        if (pressureSensor == null){
            pressure.text = "Pressure not available"
        }
        if (magneticFieldSensor == null){
            magneticField.text = "Magnetic field not available"
        }

        uplink.text = "Uplink: " + (nc.linkUpstreamBandwidthKbps/1000).toString()  + " Mbps"
        downlink.text = "Downlink: " + (nc.linkDownstreamBandwidthKbps/1000).toString() + " Mbps"
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type){
            Sensor.TYPE_MAGNETIC_FIELD -> {
                val value = event.values[0]
                magneticField.text = "Magnetic field " + value.toString() + " μT"
            }
            Sensor.TYPE_PRESSURE ->{
                val value = event.values[0]
                pressure.text = "Pressure " + value.toString() + " mbar"
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


}

