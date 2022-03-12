package com.morkov.rcrccontrol

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.slider.Slider
import java.io.IOException
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

class LandscapeMainActivity : AppCompatActivity(), View.OnClickListener,
    BluetoothController.isWriting,
    ControlPadView.PadListener, Slider.OnChangeListener {
    private var bluetoothController: BluetoothController? = null

    private lateinit var buttonFlash: ImageButton
    private lateinit var sl2: Slider
    private lateinit var sl3: Slider
    private lateinit var cpv: ControlPadView
    private lateinit var cpv2: ControlPadView
    private lateinit var cpv3: ControlPadView

    private var lightstat: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContentView(R.layout.activity_landscape_main)

        cpv = findViewById(R.id.cpv1)
        cpv2 = findViewById(R.id.cpv2)
        cpv3 = findViewById(R.id.cpv3)

        buttonFlash = findViewById(R.id.but_flash)
        buttonFlash.setOnClickListener(this)

        sl2 = findViewById(R.id.slider2)
        sl3 = findViewById(R.id.slider3)

        sl2.addOnChangeListener(this)
        sl3.addOnChangeListener(this)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> exitProcess(-1)
            R.id.menu_connect -> {
                /*val intentOpenBluetoothSettings = Intent()
                intentOpenBluetoothSettings.action = Settings.ACTION_BLUETOOTH_SETTINGS
                startActivity(intentOpenBluetoothSettings)*/
                val dca = DeviceChooseActivity()
                val intent = Intent(this, dca.javaClass)
                startActivityForResult(intent, 1)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return else {
            val device = data.getParcelableExtra<BluetoothDevice>("device")
            try {
                bluetoothController = BluetoothController(device!!)
                /* try {
                     bluetoothController!!.socket?.close()
                 } catch (e: IOException) {
                     e.printStackTrace()
                     return
                 }*/
            } catch (e: IOException) {
                Log.e("DEBUG", "Error occurred when connecting to device", e)
                Toast.makeText(
                    this,
                    resources.getText(R.string.err_when_connect),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }
        writeStringWithTag("85", "1")
        writeStringWithTag("90", "2")
        writeStringWithTag("90", "3")
        writeStringWithTag("80", "4")
        writeStringWithTag("85", "7")
        writeStringWithTag("85", "8")
    }

    override fun onClick(v: View?) {
        if (v == findViewById<ImageButton>(R.id.but_flash)) {
            writeStringWithTag("l", "l")
            if (lightstat) {
                lightstat = false
                buttonFlash.setImageResource(R.drawable.ic_baseline_flashlight_off_24)
            } else {
                lightstat = true
                buttonFlash.setImageResource(R.drawable.ic_baseline_flashlight_on_24)
            }
        }
    }

    override fun writeString(text: String?) {
        bluetoothController?.writeBTString(text)
    }

    override fun onPadMoved(xPercent: Float, yPercent: Float, id: Int) {
        when (id) {
            cpv.id -> {
                val xText = coordToString(xPercent)
                val yText = coordToString(yPercent)
                writeStringWithTag("$xText $yText", "j")
            }
            cpv2.id -> {
                val xText = (xPercent+1) * 11+74
                val yText = (yPercent+1) * 11+74/*временно разделить на 4*/

                //writeStringWithTag()
                Log.d("cpv2", "$xText         $yText")
                writeStringWithTag(percentToString(yText), "7")
                writeStringWithTag(percentToString(xText), "8")
                //TODO("ДЕЛИМ НА 2")
            }
            cpv3.id -> {//camera
                val xText = (xPercent+1) * 11+74
                val yText = (yPercent+1) * 11+69/*временно разделить на 4*/

                //writeStringWithTag()
                Log.d("cpv2", "$xText         $yText")
                writeStringWithTag(percentToString(yText), "4")
                writeStringWithTag(percentToString(xText), "1")
                //TODO("ДЕЛИМ НА 2")
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        when (slider) {
            findViewById<Slider>(R.id.slider2) -> {
                writeStringWithTag(percentToString(value), "2")

            }
            findViewById<Slider>(R.id.slider3) -> {
                writeStringWithTag(percentToString(value), "3")

            }
        }
    }

    private fun writeStringWithTag(text: String?, tag: String) {
        writeString("$tag $text")
    }

    private fun percentToString(percen: Float): String {
        val percent = percen.toInt()
        var tot: String = ""
        if (percent >= 0) {
            if (percent.absoluteValue < 100)
                tot = " 0$percent"
            if (percent.absoluteValue < 10)
                tot = " 00$percent"

        }
        if (percent >= 100) {
            tot = " $percent"
        }
        return tot
    }

    private fun coordToString(coord: Float): String {
        val coorInt = (coord * 100).toInt()

        var tot: String = ""
        if (coorInt >= 0) {
            if (coorInt.absoluteValue < 100)
                tot = " 0$coorInt"
            if (coorInt.absoluteValue < 10)
                tot = " 00$coorInt"

        }
        if (coorInt < 0) {
            if (coorInt.absoluteValue < 100)
                tot = "-0${coorInt.absoluteValue}"
            if (coorInt.absoluteValue < 10)
                tot = "-00${coorInt.absoluteValue}"

        }
        return tot
    }
}