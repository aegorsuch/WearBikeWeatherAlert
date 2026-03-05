package com.example.wearbikeweatheralert

import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.UserActivityInfo
import androidx.health.services.client.data.UserActivityState
import androidx.health.services.client.data.ExerciseType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BikeDetectionService : PassiveListenerService() {
    override fun onUserActivityInfoReceived(info: UserActivityInfo) {
        // Detect if the user started a Biking exercise
        if (info.userActivityState == UserActivityState.USER_ACTIVITY_START &&
            info.exerciseType == ExerciseType.BIKING) {

            // Immediately schedule the weather check
            val weatherRequest = OneTimeWorkRequestBuilder<WeatherWorker>().build()
            WorkManager.getInstance(this).enqueue(weatherRequest)
        }
    }
}