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
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class NetworkSpeedOverlayService : Service() {
    
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var speedTextView: TextView? = null
    
    private var executor: ScheduledExecutorService? = null
    private var handler: Handler? = null
    
    private var lastRxBytes = 0L
    private var lastTxBytes = 0L
    private var lastTime = 0L
    
    // Dragging variables
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var isDragging = false
    
    // Performance and state management
    private val isServiceRunning = AtomicBoolean(false)
    private val isOverlayVisible = AtomicBoolean(false)
    private var lastNetworkType = ""
    private var lastTextColor = 0
    
    // Advanced performance optimizations
    private var lastUpdateTime = 0L
    private var updateCount = 0L
    private var performanceMetrics = PerformanceMetrics()
    private val updateThreshold = 1000L // 1 second minimum between updates
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        handler = Handler(Looper.getMainLooper())
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand")
        if (!isServiceRunning.get()) {
            isServiceRunning.set(true)
            showOverlay()
            startNetworkSpeedMonitoring()
        }
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
        if (isOverlayVisible.get()) {
            Log.d(TAG, "Overlay already visible, skipping")
            return
        }
        
        try {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            
            val inflater = LayoutInflater.from(this)
            overlayView = inflater.inflate(R.layout.network_speed_overlay, null)
            
            speedTextView = overlayView?.findViewById(R.id.tvNetworkSpeed)
            
            // Set up touch handling for dragging
            setupTouchHandling()
            
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
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            )
            
            params.gravity = Gravity.TOP or Gravity.END
            
            // Use positioning helper for customizable positioning
            params.x = PositioningHelper.getXOffset(this)
            // Start slightly below status bar to avoid touch conflicts
            val statusBarHeight = getStatusBarHeight()
            params.y = if (PositioningHelper.getYOffset(this) == 0) {
                statusBarHeight + 5  // Position just below status bar initially
            } else {
                PositioningHelper.getYOffset(this)
            }
            
            Log.d(TAG, "Positioning overlay at x: ${params.x}, y: ${params.y}")
            
            // Additional positioning adjustments
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            
            windowManager?.addView(overlayView, params)
            isOverlayVisible.set(true)
            Log.d(TAG, "Overlay added successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to show overlay", e)
            isOverlayVisible.set(false)
        }
    }
    
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    
    private fun setupTouchHandling() {
        overlayView?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Consume the touch event immediately to prevent status bar interaction
                    overlayView?.performClick()
                    val params = overlayView?.layoutParams as? WindowManager.LayoutParams
                    initialX = params?.x ?: 0
                    initialY = params?.y ?: 0
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    Log.d(TAG, "Touch down at ${event.rawX}, ${event.rawY}")
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - initialTouchX).toInt()
                    val deltaY = (event.rawY - initialTouchY).toInt()
                    
                    // Check if this is a drag gesture (moved more than 5 pixels)
                    if (!isDragging && (Math.abs(deltaX) > 5 || Math.abs(deltaY) > 5)) {
                        isDragging = true
                    }
                    
                    if (isDragging) {
                        val params = overlayView?.layoutParams as? WindowManager.LayoutParams
                        params?.let {
                            // Fix reversed dragging by subtracting deltaX instead of adding
                            it.x = initialX - deltaX
                            it.y = initialY + deltaY
                            windowManager?.updateViewLayout(overlayView, it)
                            
                            // Visual feedback during dragging
                            overlayView?.alpha = 0.8f
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Restore full opacity
                    overlayView?.alpha = 1.0f
                    
                    if (isDragging) {
                        // Save the new position
                        val params = overlayView?.layoutParams as? WindowManager.LayoutParams
                        params?.let {
                            PositioningHelper.setXOffset(this, it.x)
                            PositioningHelper.setYOffset(this, it.y)
                            Log.d(TAG, "Position saved: x=${it.x}, y=${it.y}")
                        }
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    overlayView?.alpha = 1.0f
                    true
                }
                else -> true
            }
        }
    }
    
    private fun tryRepositionOverlay() {
        if (overlayView != null && windowManager != null) {
            try {
                windowManager?.removeView(overlayView)
                showOverlay()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun startNetworkSpeedMonitoring() {
        if (executor?.isShutdown != false) {
            executor = Executors.newSingleThreadScheduledExecutor { r ->
                Thread(r, "NetworkSpeedMonitor").apply {
                    isDaemon = true
                    priority = Thread.NORM_PRIORITY
                }
            }
        }
        
        executor?.scheduleAtFixedRate({
            if (isServiceRunning.get() && isOverlayVisible.get()) {
                updateNetworkSpeed()
            }
        }, 0, 1, TimeUnit.SECONDS)
        
        Log.d(TAG, "Network speed monitoring started")
    }
    
    private fun updateNetworkSpeed() {
        val currentTime = System.currentTimeMillis()
        
        // Smart update throttling - only update if enough time has passed
        if (currentTime - lastUpdateTime < updateThreshold) {
            return
        }
        
        val currentRxBytes = getTotalRxBytes()
        val currentTxBytes = getTotalTxBytes()
        
        if (lastTime != 0L) {
            val timeDiff = (currentTime - lastTime) / 1000.0
            val rxDiff = currentRxBytes - lastRxBytes
            val txDiff = currentTxBytes - lastTxBytes
            
            // Avoid division by zero and invalid calculations
            if (timeDiff > 0) {
                val downloadSpeed = (rxDiff / timeDiff).toLong()
                val uploadSpeed = (txDiff / timeDiff).toLong()
                
                // Only update if there's a significant change
                if (shouldUpdateUI(downloadSpeed, uploadSpeed)) {
                    val networkType = getNetworkType()
                    
                    handler?.post {
                        updateOverlay(downloadSpeed, uploadSpeed, networkType)
                        performanceMetrics.recordUpdate(currentTime)
                    }
                }
            }
        }
        
        lastTime = currentTime
        lastRxBytes = currentRxBytes
        lastTxBytes = currentTxBytes
        lastUpdateTime = currentTime
        updateCount++
    }
    
    private fun shouldUpdateUI(downloadSpeed: Long, uploadSpeed: Long): Boolean {
        // Only update if speed changed by more than 1KB/s or 5% (whichever is smaller)
        val minChange = minOf(1024L, maxOf(downloadSpeed, uploadSpeed) / 20)
        return Math.abs(downloadSpeed - performanceMetrics.lastDownloadSpeed) > minChange ||
               Math.abs(uploadSpeed - performanceMetrics.lastUploadSpeed) > minChange
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
        val newText = "↓$downloadText ↑$uploadText"
        
        // Only update UI if text has changed
        if (speedTextView?.text.toString() != newText) {
            speedTextView?.text = newText
        }
        
        // Only update color if network type has changed
        if (lastNetworkType != networkType) {
            val textColor = when (networkType) {
                "WiFi" -> resources.getColor(R.color.download_color, null)
                "Mobile" -> resources.getColor(R.color.upload_color, null)
                "Ethernet" -> resources.getColor(R.color.download_color, null)
                else -> resources.getColor(R.color.no_network_color, null)
            }
            
            if (lastTextColor != textColor) {
                speedTextView?.setTextColor(textColor)
                lastTextColor = textColor
            }
            
            lastNetworkType = networkType
        }
    }
    
    private fun formatSpeed(bytesPerSecond: Long): String {
        return when {
            bytesPerSecond >= 1024 * 1024 -> String.format("%.1f MB", bytesPerSecond / (1024.0 * 1024.0))
            bytesPerSecond >= 1024 -> String.format("%.1f KB", bytesPerSecond / 1024.0)
            else -> "${bytesPerSecond}B"
        }
    }
    
    override fun onDestroy() {
        Log.d(TAG, "Service onDestroy")
        isServiceRunning.set(false)
        
        // Shutdown executor gracefully
        executor?.let { exec ->
            exec.shutdown()
            try {
                if (!exec.awaitTermination(2, TimeUnit.SECONDS)) {
                    exec.shutdownNow()
                }
            } catch (e: InterruptedException) {
                exec.shutdownNow()
                Thread.currentThread().interrupt()
            }
        }
        
        removeOverlay()
        super.onDestroy()
    }
    
    private fun removeOverlay() {
        if (!isOverlayVisible.get()) {
            Log.d(TAG, "Overlay already removed, skipping")
            return
        }
        
        try {
            if (overlayView != null && windowManager != null) {
                windowManager?.removeView(overlayView)
                Log.d(TAG, "Overlay removed successfully")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to remove overlay", e)
        } finally {
            overlayView = null
            speedTextView = null
            windowManager = null
            isOverlayVisible.set(false)
        }
    }
    
    companion object {
        private const val TAG = "NetworkSpeedOverlay"
        private const val CHANNEL_ID = "network_speed_overlay_channel"
        private const val NOTIFICATION_ID = 1001
    }
}

// Performance monitoring class
private class PerformanceMetrics {
    var lastDownloadSpeed = 0L
    var lastUploadSpeed = 0L
    private var updateCount = 0L
    private var lastLogTime = 0L
    
    fun recordUpdate(currentTime: Long) {
        updateCount++
        
        // Log performance metrics every 60 seconds
        if (currentTime - lastLogTime > 60000) {
            Log.d("PerformanceMetrics", "Updates: $updateCount, Avg interval: ${60000.0 / updateCount}ms")
            updateCount = 0
            lastLogTime = currentTime
        }
    }
}
