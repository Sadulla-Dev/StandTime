package com.example.standtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.standtime.standtime.StandTimeRoute
import com.example.standtime.standtime.StandTimeViewModel
import com.example.standtime.standtime.ThemeMode
import com.example.standtime.ui.theme.StandTimeTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<StandTimeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            StandTimeTheme(darkTheme = uiState.themeMode == ThemeMode.DARK) {
                StandTimeRoute(
                    state = uiState,
                    onIntent = viewModel::onIntent
                )
            }
        }
    }
}
