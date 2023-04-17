package com.example.smartpedalboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.smartpedalboard.placeholder.ProfileAdapter
import com.example.smartpedalboard.placeholder.ProfileClass
import com.example.smartpedalboard.placeholder.ProfileModel

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqliteHelper = ProfileClass(requireActivity())
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

        if(name.isEmpty() || effect1.isEmpty() || effect2.isEmpty())
        {
            Toast.makeText(this.context,"Don't Forget A Name!",Toast.LENGTH_SHORT).show()
        }
        else
        {
            val pf = ProfileModel(name = name, effect1 = effect1, effect2 = effect2)
            val status = sqliteHelper.insertProfile(pf)
            if(status > -1) {
                Toast.makeText(this.context,"Profile Created!!",Toast.LENGTH_SHORT).show()
                getProfiles()
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
            addProfile()
            Navigation.findNavController(view).navigate(R.id.action_createFragment_to_profileFragment2)
        }
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