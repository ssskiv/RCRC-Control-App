package com.morkov.rcrccontrol

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import java.io.IOException
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener, BluetoothController.isWriting,
    ControlPadView.PadListener, Slider.OnChangeListener {

    private var bluetoothController: BluetoothController? = null

    private lateinit var buttonFlash: ImageButton
    private lateinit var sl1: Slider
    private lateinit var sl2: Slider
    private lateinit var sl3: Slider
    private lateinit var sl4: Slider
    private lateinit var sl5: Slider
    private lateinit var sl6: Slider
    private lateinit var sl7: Slider
    private lateinit var sl8: Slider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sl1 = findViewById(R.id.slider1)
        sl2 = findViewById(R.id.slider2)
        sl3 = findViewById(R.id.slider3)
        sl4 = findViewById(R.id.slider4)
        sl5 = findViewById(R.id.slider5)
        sl6 = findViewById(R.id.slider6)
        sl7 = findViewById(R.id.slider7)
        sl8 = findViewById(R.id.slider8)
        sl1.addOnChangeListener(this)
        sl2.addOnChangeListener(this)
        sl3.addOnChangeListener(this)
        sl4.addOnChangeListener(this)
        sl5.addOnChangeListener(this)
        sl6.addOnChangeListener(this)
        sl7.addOnChangeListener(this)
        sl8.addOnChangeListener(this)

        buttonFlash = findViewById(R.id.but_flash)
        buttonFlash.setOnClickListener(this)
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
            /*try {
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                socket.connect();
                outStream = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("DEBUG", "Error occurred when connecting to device", e);

                // Send a failure message back to the activity.
                Toast.makeText(
                        this,
                        "Error occurred when connecting to device",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
            Log.d("DEBUG", "Connected to " + device.getName() + " with type " + device.getType());
            Log.d("CONTROL", ("Connected to " + device.getName()));
            writeBTString("connected");*/
        }
    }

    override fun onClick(v: View?) {
        if (v == findViewById(R.id.but_flash)) {

            writeStringWithTag("f", "f")
        }
    }

    override fun writeString(text: String?) {
        bluetoothController?.writeBTString(text)
    }

    override fun onPadMoved(xPercent: Float, yPercent: Float, id: Int) {
        var xText = coordToString(xPercent)

        val yText = coordToString(yPercent)

        writeStringWithTag(
            "$xText $yText", "j"
        )

    }

    private fun writeStringWithTag(text: String?, tag: String) {
        writeString("$tag $text")
    }
private fun percentToString(percen:Float):String{
    val percent = percen.toInt()
    var tot: String = ""
    if (percent >= 0) {
        if (percent.absoluteValue < 100)
            tot = " 0$percent"
        if (percent.absoluteValue < 10)
            tot = " 00$percent"

    }
    if (percent >= 100) {
        tot=" $percent"
    }
    return tot
}
    private fun coordToString(coord: Float): String {
        var coorInt = (coord * 100).toInt()

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

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        when (slider) {
            findViewById<Slider>(R.id.slider1) -> {
                writeStringWithTag(percentToString(value), "1")

            }
            findViewById<Slider>(R.id.slider2) -> {
                writeStringWithTag(percentToString(value), "2")

            }
            findViewById<Slider>(R.id.slider3) -> {
                writeStringWithTag(percentToString(value), "3")

            }
            findViewById<Slider>(R.id.slider4) -> {
                writeStringWithTag(percentToString(value), "4")

            }
            findViewById<Slider>(R.id.slider5) -> {
                writeStringWithTag(percentToString(value), "5")

            }
            findViewById<Slider>(R.id.slider6) -> {
                writeStringWithTag(percentToString(value), "6")

            }
            findViewById<Slider>(R.id.slider7) -> {
                writeStringWithTag(percentToString(value), "7")

            }
            findViewById<Slider>(R.id.slider8) -> {
                writeStringWithTag(percentToString(value), "8")

            }
        }
    }


}