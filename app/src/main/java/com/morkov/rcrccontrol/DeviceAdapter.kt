package com.morkov.rcrccontrol

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(var devices: Set<BluetoothDevice>, cellClickListener: CellClickListener) :
    RecyclerView.Adapter<DeviceAdapter.DeviceHolder>() {
    var cellClickListener: CellClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.device, parent, false)
        return DeviceHolder(view)
    }

    override fun onBindViewHolder(
        holder: DeviceHolder,
        position: Int
    ) {
        val device = devices.toTypedArray()[position]
        holder.name.setText(device.name)
        holder.addr.setText(device.address)
        holder.itemView.setOnClickListener(View.OnClickListener {
            cellClickListener.onCellClickListener(
                device
            )
        } /*(View.OnClickListener) cellClickListener.onCellClickListener(device)*/)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    class DeviceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addr = itemView.findViewById<TextView>(R.id.device_addr)
        var name = itemView.findViewById<TextView>(R.id.device_name)
    }

    init {
        this.devices = devices
        this.cellClickListener = cellClickListener
    }
}