package com.wearbikeweatheralert.wearbikeweatheralert

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WeatherWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val apiKey = "YOUR_OPENWEATHER_API_KEY" // Get this from openweathermap.org
        val lat = 41.87 // We'll eventually replace these with real GPS
        val lon = -87.62

        return try {
            val response = RetrofitClient.instance.getWeather(lat, lon, apiKey = apiKey)

            // Check the next 8 hours for any rain chance > 30%
            val rainFound = response.hourly.take(8).any { it.pop >= 0.30 }

            if (rainFound) {
                NotificationHelper.showRainAlert(applicationContext)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
} {
}