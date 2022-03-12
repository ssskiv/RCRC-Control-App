package com.morkov.rcrccontrol

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.slider.Slider
import java.io.IOException
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener, BluetoothController.isWriting,
    ControlPadView.PadListener, Slider.OnChangeListener {

    private var bluetoothController: BluetoothController? = null

    private lateinit var buttonFlash: ImageButton
    private lateinit var buttonZero: Button
    private lateinit var button360: Button
    private lateinit var sl1: Slider
    private lateinit var sl2: Slider
    private lateinit var sl3: Slider
    private lateinit var sl4: Slider
    private lateinit var sl7: Slider
    private lateinit var sl8: Slider
    private lateinit var sl1plbut: ImageButton
    private lateinit var sl2plbut: ImageButton
    private lateinit var sl3plbut: ImageButton
    private lateinit var sl4plbut: ImageButton
    private lateinit var sl7plbut: ImageButton
    private lateinit var sl8plbut: ImageButton
    private lateinit var sl1minbut: ImageButton
    private lateinit var sl2minbut: ImageButton
    private lateinit var sl3minbut: ImageButton
    private lateinit var sl4minbut: ImageButton
    private lateinit var sl7minbut: ImageButton
    private lateinit var sl8minbut: ImageButton

    private var lightstat: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContentView(R.layout.activity_main)
        sl1 = findViewById(R.id.slider1)
        sl2 = findViewById(R.id.slider2)
        sl3 = findViewById(R.id.slider3)
        sl4 = findViewById(R.id.slider4)
        sl7 = findViewById(R.id.slider7)
        sl8 = findViewById(R.id.slider8)
        sl1.addOnChangeListener(this)
        sl2.addOnChangeListener(this)
        sl3.addOnChangeListener(this)
        sl4.addOnChangeListener(this)
        sl7.addOnChangeListener(this)
        sl8.addOnChangeListener(this)

        buttonFlash = findViewById(R.id.but_flash)
        buttonFlash.setOnClickListener(this)
        buttonZero = findViewById(R.id.zero_button)
        buttonZero.setOnClickListener(this)
        button360 = findViewById(R.id.rev_button)
        button360.setOnClickListener(this)

        sl1plbut = findViewById(R.id.slider1_button_plus)
        sl2plbut = findViewById(R.id.slider2_button_plus)
        sl3plbut = findViewById(R.id.slider3_button_plus)
        sl4plbut = findViewById(R.id.slider4_button_plus)
        sl7plbut = findViewById(R.id.slider7_button_plus)
        sl8plbut = findViewById(R.id.slider8_button_plus)
        sl1minbut = findViewById(R.id.slider1_button_minus)
        sl2minbut = findViewById(R.id.slider2_button_minus)
        sl3minbut = findViewById(R.id.slider3_button_minus)
        sl4minbut = findViewById(R.id.slider4_button_minus)
        sl7minbut = findViewById(R.id.slider7_button_minus)
        sl8minbut = findViewById(R.id.slider8_button_minus)
        sl1plbut.setOnClickListener(this)
        sl2plbut.setOnClickListener(this)
        sl3plbut.setOnClickListener(this)
        sl4plbut.setOnClickListener(this)
        sl7plbut.setOnClickListener(this)
        sl8plbut.setOnClickListener(this)
        sl1minbut.setOnClickListener(this)
        sl2minbut.setOnClickListener(this)
        sl3minbut.setOnClickListener(this)
        sl4minbut.setOnClickListener(this)
        sl7minbut.setOnClickListener(this)
        sl8minbut.setOnClickListener(this)
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
        when (v) {
            findViewById<ImageButton>(R.id.but_flash) -> {
                writeStringWithTag("l", "l")
                if (lightstat) {
                    lightstat = false
                    buttonFlash.setImageResource(R.drawable.ic_baseline_flashlight_off_24)
                } else {
                    lightstat = true
                    buttonFlash.setImageResource(R.drawable.ic_baseline_flashlight_on_24)
                }
            }
            findViewById<Button>(R.id.zero_button) -> {
                Log.d("CONTROL", "ZERO")
                sl1.value = 85F
                sl2.value = 90F
                sl3.value = 90F
                sl4.value = 80F
                sl7.value = 85F
                sl8.value = 85F
            }
            findViewById<Button>(R.id.rev_button) -> {
                Log.d("CONTROL", "ZERO_360")
                sl1.value = 85F
                sl4.value = 80F
                sl7.value = 85F
                sl8.value = 85F
            }
            findViewById<ImageButton>(R.id.slider1_button_plus) -> {
                if (sl1.value + sl1.stepSize <= sl1.valueTo)
                    sl1.value += sl1.stepSize
            }
            findViewById<ImageButton>(R.id.slider2_button_plus) -> {
                if (sl2.value + sl2.stepSize <= sl2.valueTo)
                    sl2.value += sl2.stepSize
            }
            findViewById<ImageButton>(R.id.slider3_button_plus) -> {
                if (sl3.value + sl3.stepSize <= sl3.valueTo)
                    sl3.value += sl3.stepSize
            }
            findViewById<ImageButton>(R.id.slider4_button_plus) -> {
                if (sl4.value + sl4.stepSize <= sl4.valueTo)
                    sl4.value += sl4.stepSize
            }
            findViewById<ImageButton>(R.id.slider7_button_plus) -> {
                if (sl7.value + sl7.stepSize <= sl7.valueTo)
                    sl7.value += sl7.stepSize
            }
            findViewById<ImageButton>(R.id.slider8_button_plus) -> {
                if (sl8.value + sl8.stepSize <= sl8.valueTo)
                    sl8.value += sl8.stepSize
            }
            findViewById<ImageButton>(R.id.slider1_button_minus) -> {
                if (sl8.value - sl8.stepSize >= sl8.valueFrom)
                    sl1.value -= sl1.stepSize
            }
            findViewById<ImageButton>(R.id.slider2_button_minus) -> {
                if (sl2.value - sl2.stepSize >= sl2.valueFrom)
                    sl2.value -= sl2.stepSize
            }
            findViewById<ImageButton>(R.id.slider3_button_minus) -> {
                if (sl3.value - sl3.stepSize >= sl3.valueFrom)
                    sl3.value -= sl3.stepSize
            }
            findViewById<ImageButton>(R.id.slider4_button_minus) -> {
                if (sl4.value - sl4.stepSize >= sl4.valueFrom)
                    sl4.value -= sl4.stepSize
            }
            findViewById<ImageButton>(R.id.slider7_button_minus) -> {
                if (sl7.value - sl7.stepSize >= sl7.valueFrom)
                    sl7.value -= sl7.stepSize
            }
            findViewById<ImageButton>(R.id.slider8_button_minus) -> {
                if (sl8.value - sl8.stepSize >= sl8.valueFrom)
                    sl8.value -= sl8.stepSize
            }
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

    @SuppressLint("RestrictedApi")
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
            findViewById<Slider>(R.id.slider7) -> {
                writeStringWithTag(percentToString(value), "7")

            }
            findViewById<Slider>(R.id.slider8) -> {
                writeStringWithTag(percentToString(value), "8")

            }
        }
    }

}