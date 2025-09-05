package com.networkspeed.overlay

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class NetworkSpeedOverlayServiceV2 : Service() {
    
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var speedTextView: TextView? = null
    
    private var executor: ScheduledExecutorService? = null
    private var handler: Handler? = null
    
    private var lastRxBytes = 0L
    private var lastTxBytes = 0L
    private var lastTime = 0L
    
    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showOverlay()
        startNetworkSpeedMonitoring()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Network Speed Overlay",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows network speed on status bar"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Network Speed Overlay")
            .setContentText("Monitoring network speed...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
    
    private fun showOverlay() {
        if (overlayView != null) return
        
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.network_speed_overlay, null)
        
        speedTextView = overlayView?.findViewById(R.id.tvNetworkSpeed)
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 16
        params.y = 0  // Try positioning at the very top
        
        android.util.Log.d("NetworkSpeedOverlay", "Positioning overlay at very top: x=${params.x}, y=${params.y}")
        
        try {
            windowManager?.addView(overlayView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun startNetworkSpeedMonitoring() {
        executor = Executors.newSingleThreadScheduledExecutor()
        executor?.scheduleAtFixedRate({
            updateNetworkSpeed()
        }, 0, 1, TimeUnit.SECONDS)
    }
    
    private fun updateNetworkSpeed() {
        val currentTime = System.currentTimeMillis()
        val currentRxBytes = getTotalRxBytes()
        val currentTxBytes = getTotalTxBytes()
        
        if (lastTime != 0L) {
            val timeDiff = (currentTime - lastTime) / 1000.0
            val rxDiff = currentRxBytes - lastRxBytes
            val txDiff = currentTxBytes - lastTxBytes
            
            val downloadSpeed = (rxDiff / timeDiff).toLong()
            val uploadSpeed = (txDiff / timeDiff).toLong()
            
            val networkType = getNetworkType()
            
            handler?.post {
                updateOverlay(downloadSpeed, uploadSpeed, networkType)
            }
        }
        
        lastTime = currentTime
        lastRxBytes = currentRxBytes
        lastTxBytes = currentTxBytes
    }
    
    private fun getTotalRxBytes(): Long {
        return try {
            val stats = android.net.TrafficStats.getTotalRxBytes()
            if (stats == android.net.TrafficStats.UNSUPPORTED.toLong()) 0L else stats
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getTotalTxBytes(): Long {
        return try {
            val stats = android.net.TrafficStats.getTotalTxBytes()
            if (stats == android.net.TrafficStats.UNSUPPORTED.toLong()) 0L else stats
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getNetworkType(): String {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return "No Network"
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "No Network"
        
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Unknown"
        }
    }
    
    private fun updateOverlay(downloadSpeed: Long, uploadSpeed: Long, networkType: String) {
        val downloadText = formatSpeed(downloadSpeed)
        val uploadText = formatSpeed(uploadSpeed)
        
        speedTextView?.text = "↓$downloadText ↑$uploadText"
        
        // Update colors based on network type
        val textColor = when (networkType) {
            "WiFi" -> resources.getColor(R.color.download_color, null)
            "Mobile" -> resources.getColor(R.color.upload_color, null)
            "Ethernet" -> resources.getColor(R.color.download_color, null)
            else -> resources.getColor(R.color.no_network_color, null)
        }
        
        speedTextView?.setTextColor(textColor)
    }
    
    private fun formatSpeed(bytesPerSecond: Long): String {
        return when {
            bytesPerSecond >= 1024 * 1024 -> String.format("%.1f MB", bytesPerSecond / (1024.0 * 1024.0))
            bytesPerSecond >= 1024 -> String.format("%.1f KB", bytesPerSecond / 1024.0)
            else -> "${bytesPerSecond}B"
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        executor?.shutdown()
        removeOverlay()
    }
    
    private fun removeOverlay() {
        try {
            if (overlayView != null && windowManager != null) {
                windowManager?.removeView(overlayView)
                overlayView = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "network_speed_overlay_channel"
        private const val NOTIFICATION_ID = 1001
    }
}
