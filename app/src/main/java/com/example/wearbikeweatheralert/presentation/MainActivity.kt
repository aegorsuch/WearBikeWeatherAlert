package com.example.wearbikeweatheralert.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.wearbikeweatheralert.BikeDetectionService
import com.example.wearbikeweatheralert.presentation.theme.WearBikeWeatherAlertTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearBikeWeatherAlertTheme {
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    if (permissions.all { it.value }) {
                        initializeBikeDetection()
                    }
                }

                LaunchedEffect(Unit) {
                    val permissions = mutableListOf(
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        Manifest.permission.BODY_SENSORS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    permissionLauncher.launch(permissions.toTypedArray())
                }

                WearApp("Cyclist")
            }
        }
    }

    private fun initializeBikeDetection() {
        val passiveClient = HealthServices.getClient(this).passiveMonitoringClient
        val config = PassiveListenerConfig.builder()
            .setShouldUserActivityInfoBeRequested(true)
            .setDataTypes(setOf(DataType.HEART_RATE_BPM, DataType.STEPS_DAILY))
            .build()

        lifecycleScope.launch {
            passiveClient.setPassiveListenerServiceAsync(BikeDetectionService::class.java, config)
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello $greetingName, watching for bike rides!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}