package com.example.wearbikeweatheralert

import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.UserActivityInfo
import androidx.health.services.client.data.UserActivityState
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BikeDetectionService : PassiveListenerService() {
    override fun onUserActivityInfoReceived(info: UserActivityInfo) {
        // Detect if the user is currently exercising.
        // Health Services doesn't always provide the specific ExerciseType in passive mode 
        // without a specific ExerciseUpdate, but we can check if the state switched to EXERCISE.
        if (info.userActivityState == UserActivityState.USER_ACTIVITY_EXERCISE) {
            // Schedule the weather check
            val weatherRequest = OneTimeWorkRequestBuilder<WeatherWorker>().build()
            WorkManager.getInstance(this).enqueue(weatherRequest)
        }
    }
}