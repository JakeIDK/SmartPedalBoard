package com.example.smartpedalboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private lateinit var textName: EditText
    private lateinit var sEf1: Spinner
    private lateinit var sEf2: Spinner
    private lateinit var out: OutputStream
    private lateinit var btDevice: BluetoothDevice
    private lateinit var mmSocket: BluetoothSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val btManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        if (btAdapter == null) {
            // Device doesn't support Bluetooth
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(ACTION_DISCOVERY_FINISHED)
        filter.addAction(ACTION_ACL_DISCONNECTED)
        context?.registerReceiver(receiver, filter)
        if(!btAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)

        }


        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        //important to unregister receiver
        context?.unregisterReceiver(receiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        sEf1 = view.findViewById<Spinner>(R.id.spinnerEffect1)
        sEf2 = view.findViewById<Spinner>(R.id.spinnerEffect2)

        if (btAdapter == null) {
            // Device doesn't support Bluetooth
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        context?.registerReceiver(receiver, filter)

        if(!btAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)

        }

        val button1 = view.findViewById<Button>(R.id.btnGet)
        val button2 = view.findViewById<Button>(R.id.btnConnect)
        val button3 =view.findViewById<Button>(R.id.btnSave)

        super.onViewCreated(view, savedInstanceState)
        button1.setOnClickListener {
            val pairedDevices: Set<BluetoothDevice>? = if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setOnClickListener
            }
            else{
                null
            }
            btAdapter.bondedDevices
            if (pairedDevices != null) {
                for (dv in pairedDevices)
                {
                    if(dv.name == "HC-05")
                    {
                        btDevice = dv
                        mmSocket = btDevice!!.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                    }
                }
            }
        }
        button2.setOnClickListener {
            try
            {
                mmSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"))
                mmSocket?.connect()
                val out = mmSocket?.outputStream
                Toast.makeText(requireContext(),"Device Connected!!",Toast.LENGTH_SHORT).show()
            }catch (closeException: IOException){}
        }
        button3.setOnClickListener {
            sendData()
        }
    }
    private fun sendData()
    {
        var data = byteArrayOf(0,0)

        when(sEf1.selectedItem.toString())
        {
            "Clean(No effect)"-> data[0] = 1
            "Delay"-> data[0] = 2
            "Distort"-> data[0] = 3
            "Tremelo"-> data[0] = 4
        }
        when(sEf2.selectedItem.toString())
        {
            "Clean(No effect)"-> data[1] = 1
            "Delay"-> data[1] = 2
            "Distort"-> data[1] = 3
            "Tremelo"-> data[1] = 4
        }
        out.write(data)
    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}