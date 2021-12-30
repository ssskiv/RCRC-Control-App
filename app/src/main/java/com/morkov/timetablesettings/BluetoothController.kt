package com.morkov.timetablesettings


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket

import java.io.IOException
import java.io.OutputStream
import java.util.*


open class BluetoothController internal constructor(val device: BluetoothDevice) {
    var socket: BluetoothSocket? = null
    private val outStream: OutputStream

    @Throws(IOException::class)
    protected fun writeBT(bytes: ByteArray?) {
        outStream.write(bytes)
    }

    @Throws(IOException::class)
    fun writeBTString(text: String?) {
        val txt = " $text\n"
        writeBT(txt.toByteArray())
    }

    interface isWriting {
        fun writeString(text: String?)
    }

    init {

        socket =
            device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

        socket!!.connect()
        outStream = socket!!.outputStream
    }
}