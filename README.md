# WearBikeWeatherAlert 🚴‍♂️🌦️

A Wear OS application that automatically monitors for biking workouts and alerts you if rain is predicted later in the day.

## How it works

1.  **Biking Detection**: Uses Android Health Services' `PassiveMonitoringClient` to detect when a biking exercise starts (works with Samsung Health and other Wear OS trackers).
2.  **Weather Check**: Once a ride is detected, a background `WorkManager` task fetches your current location using Google Play Services.
3.  **Rain Alert**: The app calls the OpenWeatherMap One Call API to check the hourly forecast. If the probability of precipitation exceeds 30% in the next 8 hours, you receive a notification on your watch.

## Setup

To protect sensitive information, this project uses the `Secrets Gradle Plugin`.

1.  **Get an API Key**: Sign up for a free One Call 3.0 API key at [openweathermap.org](https://openweathermap.org/api/one-call-3).
2.  **Create a secrets file**: In the root directory of the project, create a file named `secrets.properties`.
3.  **Add your key**:
    ```properties
    WEATHER_API_KEY=your_actual_api_key_here
    ```
4.  **Build**: Sync Gradle and run the app on your Wear OS device.

## Permissions Required

*   **Activity Recognition**: To detect when you start biking.
*   **Body Sensors**: Required for Health Services monitoring.
*   **Location**: To get the local weather forecast.
*   **Notifications**: To send you the rain alert.

## Technologies

*   Kotlin
*   Jetpack Compose for Wear OS
*   Health Services (Passive Monitoring)
*   WorkManager
*   Retrofit & Gson
*   Google Play Services Location
*   Secrets Gradle Plugin
