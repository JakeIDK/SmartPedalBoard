package com.example.smartpedalboard

import android.bluetooth.BluetoothSocket
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.smartpedalboard.ProfileClasses.ProfileAdapter
import com.example.smartpedalboard.ProfileClasses.ProfileClass
import com.example.smartpedalboard.ProfileClasses.ProfileModel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files.write

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var textName: EditText
    private lateinit var sEf1: Spinner
    private lateinit var sEf2: Spinner
    private lateinit var sqliteHelper : ProfileClass
    private var adapter: ProfileAdapter? = null
    private var pfd: ProfileModel? = null
    private var send:BTservice.ConnectedThread? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sqliteHelper = ProfileClass(requireContext())


        adapter?.setOnClickItem { Toast.makeText(requireActivity(),it.name, Toast.LENGTH_SHORT).show()
            textName.setText(it.name)
            //sEf1.setText(it.effect1)
            //sEf2.setText(it.effect2)
            pfd = it
        }

        arguments?.let {
          //  param1 = it.getString(ARG_PARAM1)
           // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_create, container, false)
    }
    private fun getProfiles()
    {
        val pfList = sqliteHelper.getAllProfile()
        adapter?.addItems(pfList)
    }
    private fun addProfile()
    {

        val name = textName.text.toString()
        val effect1 = sEf1.selectedItem.toString()
        val effect2 = sEf2.selectedItem.toString()

        if(name.isEmpty())
        {
            Toast.makeText(this.context,"Don't Forget A Name!",Toast.LENGTH_SHORT).show()
        }
        else
        {
            val pf = ProfileModel(name = name, effect1 = effect1, effect2 = effect2)
            val status = sqliteHelper.insertProfile(pf)
            if(status > -1) {
                Toast.makeText(this.context,"Profile Created!!",Toast.LENGTH_SHORT).show()
                textName.text.clear()

            }
            else{
                Toast.makeText(this.context,"Profile not created...",Toast.LENGTH_SHORT).show()

            }
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textName = view.findViewById<EditText>(R.id.textName)
        sEf1 = view.findViewById<Spinner>(R.id.spinnerEffect1)
        sEf2 = view.findViewById<Spinner>(R.id.spinnerEffect2)
        val button1 = view.findViewById<Button>(R.id.buttonSave)
        super.onViewCreated(view, savedInstanceState)
        button1.setOnClickListener {
           /* addProfile()
           // getProfiles()
            Navigation.findNavController(view).navigate(R.id.action_createFragment_to_profileFragment2)
            */
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
        send!!.write(data)
    }


        companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateFragment().apply {
                arguments = Bundle().apply {
                 //   putString(ARG_PARAM1, param1)
                  //  putString(ARG_PARAM2, param2)
                }
            }
    }
}

