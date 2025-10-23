package com.example.multiplechoicesrs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.multiplechoicesrs.ui.theme.MultipleChoiceSRSTheme
import com.example.multiplechoicesrs.view.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultipleChoiceSRSTheme {
                App()
            }
        }
    }
}
