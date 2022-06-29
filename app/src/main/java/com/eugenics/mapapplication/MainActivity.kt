package com.eugenics.mapapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.eugenics.mapapplication.navigation.NavGraph
import com.eugenics.mapapplication.ui.theme.MapApplicationTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            MapApplicationTheme {
                NavGraph(
                    navController = navController
                )
            }
        }
    }
}