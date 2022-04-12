package com.example.tp2.fragments

import android.app.Dialog
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.tp2.MainActivity
import com.example.tp2.R
import com.example.tp2.classes.Sensors


class AnalyticsFragment : DialogFragment(){
    private lateinit var nc: NetworkCapabilities
    private lateinit var magneticField: TextView
    private lateinit var steps: TextView
    private lateinit var battery: TextView
    private lateinit var uplink: TextView
    private lateinit var downlink: TextView
    private lateinit var sensors: Sensors

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensors = (this.activity as MainActivity).getSensors();
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
        steps = view.findViewById(R.id.tvSteps)
        battery = view.findViewById(R.id.tvBattery)
        uplink = view.findViewById(R.id.tvUplink)
        downlink = view.findViewById(R.id.tvDownlink)
        sensors.magneticField.observe(this, Observer {
            magneticField.text = getString(R.string.magneticField)+"$it μT"
        })
        sensors.steps.observe(this, Observer {
            steps.text = getString(R.string.numberSteps) + "$it"
        })
       /* if (pressureSensor == null){
            pressure.text = "Pressure not available"
        }
        if (magneticFieldSensor == null){
            magneticField.text = "Magnetic field not available"
        }
        */

       // uplink.text = "Uplink: " + (nc.linkUpstreamBandwidthKbps/1000).toString()  + " Mbps"
       // downlink.text = "Downlink: " + (nc.linkDownstreamBandwidthKbps/1000).toString() + " Mbps"
    }

    override fun onDestroy() {
        super.onDestroy()
        sensors.magneticField.removeObservers(this)
        sensors.steps.removeObservers(this)
    }
}

