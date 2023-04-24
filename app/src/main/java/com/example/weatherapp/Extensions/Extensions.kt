package com.example.weatherapp.Extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Activity.isPermissionGranted(p: String): Boolean {
    return ContextCompat
        .checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED
}