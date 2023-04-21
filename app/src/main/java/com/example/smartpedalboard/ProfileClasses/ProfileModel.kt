package com.example.smartpedalboard.ProfileClasses
import java.util.*
data class ProfileModel(
    var id: Int = getAutoId(),
    var name: String = "",
    var effect1: String = "",
    var effect2: String = ""){
      companion object {
          fun getAutoId(): Int
          {
              val random = Random()
              return random.nextInt(100)
          }

        }
}