package com.example.tp2.fragments

import android.app.Dialog
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
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
    private lateinit var connectivityManager: ConnectivityManager
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
        sensors = (this.activity as MainActivity).getSensors()
        connectivityManager = activity?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) as NetworkCapabilities
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

        val packageManager = context?.packageManager
        val info = packageManager?.getApplicationInfo("com.example.tp2", 0)
        val uid = info?.uid

        val received = uid?.let { TrafficStats.getUidRxBytes(it) }
        val transmitted = uid?.let { TrafficStats.getUidTxBytes(it) }

        uplink.text = getString(R.string.uplink) + (received?.div(1000)).toString() + " KB"
        downlink.text = getString(R.string.downlink) + (transmitted?.div(1000)).toString() + " KB"
        battery.text = getString(R.string.battery_consumption) + (this.activity as MainActivity).getBatteryUsage() + "%"
    }

    override fun onDestroy() {
        super.onDestroy()
        sensors.magneticField.removeObservers(this)
        sensors.steps.removeObservers(this)
    }
}

