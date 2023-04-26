package com.example.smartpedalboard.ProfileClasses


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.widget.Toast
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothConnect(private val mmDevice: BluetoothDevice) : Thread() {
        private val btAdapter = BluetoothAdapter.getDefaultAdapter()
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            mmDevice.createRfcommSocketToServiceRecord(MY_UUID)
        }

        @SuppressLint("MissingPermission")
        override fun run() {
            btAdapter.cancelDiscovery()
            try {
                mmSocket?.connect()
            } catch (connectException: IOException) {
                try {
                    mmSocket?.close()
                } catch (closeException: IOException) { }
                return
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) { }
        }
}