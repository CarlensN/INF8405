package com.example.tp2.classes

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager

class StepSensor(activity:Activity) : SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepCounter: Sensor? = null

    init {
        sensorManager =  activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun onResume(activity: Activity){
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun onPause(){
        sensorManager.unregisterListener(this, stepCounter)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type){
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    magneticField = event.values[0]
                    //magneticField.text = "Magnetic field " + value.toString() + " μT"
                }
                Sensor.TYPE_STEP_COUNTER -> {
                    val currentSteps = event.values[0].toInt()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}