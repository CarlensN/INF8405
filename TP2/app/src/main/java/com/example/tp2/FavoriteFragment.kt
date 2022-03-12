package com.example.tp2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FavoriteFragment : Fragment() {
    private lateinit var adapter: DeviceAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvFavoriteList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DeviceAdapter{ position ->  onRecyclerViewItemClick(position)}
        recyclerView.adapter = adapter
    }

    @SuppressLint("MissingPermission")
    private fun onRecyclerViewItemClick(position: Int){
        Toast.makeText(this.context, adapter.devices[position].name, Toast.LENGTH_SHORT).show()
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

}