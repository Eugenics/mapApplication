package com.eugenics.mapapplication.domain.util

object Converter {
    fun latToLatDeg(lath: Double): String {
        val value = if (lath < 0) lath * (-1.0) else lath
        val sym = if (lath > 0) "N" else "S"
        val deg = value.toInt()
        val min = ((value - deg) * 60).toInt()
        val sec = ((value - deg - (min / 60)) * 3600).toInt()
        return "$deg° $min' $sec\" $sym"
    }

    fun longToLongDeg(long: Double): String {
        val value = if (long < 0) long * (-1.0) else long
        val sym = if (long > 0) "E" else "W"
        val deg = value.toInt()
        val min = ((value - deg) * 60).toInt()
        val sec = ((value - deg - (min / 60)) * 3600).toInt()
        return "$deg° $min' $sec\" $sym"
    }
}