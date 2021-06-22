package com.tony.notdependence.data

import com.google.firebase.database.ServerValue
import java.util.*

data class Aim(
    var date: Long? = 0,
    val aim: String = "",
    val days: Map<String,String>? = null
)

//= ServerValue.TIMESTAMP