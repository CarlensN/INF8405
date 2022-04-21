package com.example.tp2.classes

import android.app.Activity
import android.content.Context
import android.database.Observable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData

import kotlin.properties.Delegates

class Sensors(activity:Activity) : SensorEventListener {
    private var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private var magneticFieldSensor: Sensor? = null
    private var stepDetector: Sensor? = null
    private var connectivityManager: ConnectivityManager

    val steps: MutableLiveData<Int> = MutableLiveData()
    val magneticField: MutableLiveData<Number> = MutableLiveData()
    var currentSteps = 0;

    init {
        connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        sensorManager =  activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    fun onResume() {
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun onPause(){
        sensorManager.unregisterListener(this, pressureSensor)
        sensorManager.unregisterListener(this, magneticFieldSensor)
        sensorManager.unregisterListener(this, stepDetector)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type){
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    magneticField.value = event.values[0]
                }
                Sensor.TYPE_STEP_DETECTOR -> {
                    currentSteps++
                    steps.setValue(currentSteps)
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}