package com.eugenics.mapapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.eugenics.mapapplication.navigation.NavGraph
import com.eugenics.mapapplication.ui.theme.MapApplicationTheme

private const val ACCESS_COARSE_LOCATION_CODE = 100
private const val ACCESS_FINE_LOCATION_CODE = 200
private const val ACCESS_BACKGROUND_LOCATION_CODE = 300

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapOf(
            ACCESS_COARSE_LOCATION_CODE to Manifest.permission.ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION_CODE to Manifest.permission.ACCESS_FINE_LOCATION,
            ACCESS_BACKGROUND_LOCATION_CODE to Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ).forEach { entry ->
            checkPermission(entry.value, entry.key)
        }

        setContent {
            val navController = rememberNavController()

            MapApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        context = this,
                        fragmentManager = supportFragmentManager
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        val check = ContextCompat.checkSelfPermission(this@MainActivity, permission)
        if (check == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Log.d("Permissions", "Permission - $permission already granted")
        }
    }
}