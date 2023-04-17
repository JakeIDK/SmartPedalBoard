package com.example.smartpedalboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.smartpedalboard.placeholder.ProfileClass

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)


    }

   /* override fun onSupportNavigateUp(): Boolean {
       // val navController =findNavController(R.id.my_nav)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }*/
}