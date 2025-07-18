package com.example.videogifwidget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

class WidgetUpdateService : Service() {
    private var updateJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPeriodicUpdates()
        return START_STICKY
    }

    private fun startPeriodicUpdates() {
        updateJob?.cancel()
        updateJob = serviceScope.launch {
            while (isActive) {
                try {
                    // Update all widgets
                    VideoGifWidgetProvider.updateAllWidgets(this@WidgetUpdateService)
                    
                    // Wait for 30 seconds before next update (for GIF animation simulation)
                    delay(30000)
                } catch (e: Exception) {
                    // Handle any errors gracefully
                    delay(60000) // Wait longer on error
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateJob?.cancel()
        serviceScope.cancel()
    }

    companion object {
        fun startService(context: android.content.Context) {
            val intent = Intent(context, WidgetUpdateService::class.java)
            context.startService(intent)
        }

        fun stopService(context: android.content.Context) {
            val intent = Intent(context, WidgetUpdateService::class.java)
            context.stopService(intent)
        }
    }
}
