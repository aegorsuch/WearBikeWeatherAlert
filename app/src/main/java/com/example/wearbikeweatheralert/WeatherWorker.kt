package com.example.wearbikeweatheralert

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.WEATHER_API_KEY
        
        if (apiKey.isEmpty() || apiKey == "null") {
            return@withContext Result.failure()
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        return@withContext try {
            // Get current location
            val locationTask = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            val location = Tasks.await(locationTask)
            
            if (location == null) {
                return@withContext Result.retry()
            }

            val response = RetrofitClient.instance.getWeather(
                lat = location.latitude,
                lon = location.longitude,
                apiKey = apiKey
            )

            // Check the next 8 hours for any rain chance > 30%
            val rainFound = response.hourly.take(8).any { it.pop >= 0.30 }

            if (rainFound) {
                NotificationHelper.showRainAlert(applicationContext)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}