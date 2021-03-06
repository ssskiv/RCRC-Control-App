package com.morkov.rcrccontrol

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.util.function.Consumer


class DeviceChooseActivity : AppCompatActivity(), CellClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_choose)
        Log.d("DEBUG", "DeviceChooseActivity onCreate: entered")
        val recyclerView = findViewById<RecyclerView>(R.id.rv_devices)
        try {
            val bluetoothController = BluetoothController()
            val deviceAdapter = DeviceAdapter(bluetoothController.pairedDevices, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = deviceAdapter
        } catch (e: IOException) {
            Toast.makeText(this, "Enable bluetooth", Toast.LENGTH_LONG).show()
        }
        // calling the action bar
        val actionBar: ActionBar? = supportActionBar
        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCellClickListener(data: BluetoothDevice?) {
        val intent = Intent()
        intent.putExtra("device", data)
        setResult(RESULT_OK, intent)
        finish()
    }



    @SuppressLint("MissingPermission")
    private class BluetoothController {
        var message: String? = null
        var device: BluetoothDevice? = null
        private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        @SuppressLint("MissingPermission")
        var pairedDevices = bluetoothAdapter.bondedDevices

        init {
            pairedDevices.forEach(Consumer { device: BluetoothDevice ->

                val deviceName = device.name
                val deviceHardwareAddress = device.address

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


interface CellClickListener {
    fun onCellClickListener(data: BluetoothDevice?)
}