package com.tony.notdependence.interfaces

import com.tony.notdependence.data.DayItem

interface OnItemClickListener {
    fun onItemClick(dayItem: DayItem, position: Int)
}