package com.example.wearbikeweatheralert

import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.UserActivityInfo
import androidx.health.services.client.data.UserActivityState
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BikeDetectionService : PassiveListenerService() {
    override fun onUserActivityInfoReceived(info: UserActivityInfo) {
        Log.d("BikeDetection", "User activity state: ${info.userActivityState.name}")
        
        // USER_ACTIVITY_EXERCISE is the most reliable state to catch when Samsung Health starts a workout
        if (info.userActivityState == UserActivityState.USER_ACTIVITY_EXERCISE) {
            Log.d("BikeDetection", "Exercise detected, scheduling weather check")
            val weatherRequest = OneTimeWorkRequestBuilder<WeatherWorker>().build()
            WorkManager.getInstance(this).enqueue(weatherRequest)
        }
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        // We can also listen for specific exercise-related data points
        // to be even more certain an exercise is happening
        Log.d("BikeDetection", "New data points received: ${dataPoints.dataTypes}")
    }
}