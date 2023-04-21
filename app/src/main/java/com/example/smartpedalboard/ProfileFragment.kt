package com.example.smartpedalboard

import android.app.AlertDialog
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpedalboard.ProfileClasses.ProfileAdapter
import com.example.smartpedalboard.ProfileClasses.ProfileClass
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var sqliteHelper : ProfileClass
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqliteHelper = ProfileClass(requireActivity())
        initRecyclerView()

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<RecyclerView>(R.id.dbRecycler).also { recyclerView = it }
        val save = view.findViewById<Button>(R.id.buttonSend)
        //val delete = view.findViewById<Button>(R.id.buttonDelete)
    }
    private fun initRecyclerView()
    {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProfileAdapter()
        recyclerView.adapter = adapter
        adapter?.setOnClickItem { Toast.makeText(requireActivity(),it.name, Toast.LENGTH_SHORT).show() }
    }

    private fun deleteProfile(id:Int)
    {
        if(id ==null) return
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Want to delete?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog,_->
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog,_->
            dialog.dismiss()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                  //  putString(ARG_PARAM1, param1)
                  //  putString(ARG_PARAM2, param2)
                }
            }
    }
}