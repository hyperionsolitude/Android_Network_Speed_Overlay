package com.networkspeed.overlay

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.networkspeed.overlay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (checkSystemOverlayPermission()) {
            binding.btnStartOverlay.text = getString(R.string.start_overlay)
            Toast.makeText(this, "Permission granted! You can now start the overlay.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.overlay_permission_required), Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        checkPermissions()
        
        // Memory optimization - enable hardware acceleration
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources
        binding.root.removeAllViews()
    }
    
    private fun setupClickListeners() {
        binding.btnStartOverlay.setOnClickListener {
            if (checkSystemOverlayPermission()) {
                startNetworkSpeedOverlay()
            } else {
                requestSystemOverlayPermission()
            }
        }
        
        binding.btnStopOverlay.setOnClickListener {
            stopNetworkSpeedOverlay()
        }
        
        binding.btnGrantAccessibility.setOnClickListener {
            openAccessibilitySettings()
        }
        
        binding.btnResetPosition.setOnClickListener {
            resetPosition()
        }
    }
    
    private fun checkPermissions() {
        if (!checkSystemOverlayPermission()) {
            binding.btnStartOverlay.text = getString(R.string.grant_permission)
        }
    }
    
    private fun checkSystemOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
    
    private fun requestSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            overlayPermissionLauncher.launch(intent)
        }
    }
    
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
        Toast.makeText(this, "Please enable Network Speed Overlay accessibility service", Toast.LENGTH_LONG).show()
    }
    
    private fun startNetworkSpeedOverlay() {
        val intent = Intent(this, NetworkSpeedOverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Network Speed Overlay started - Drag to reposition, use app to stop", Toast.LENGTH_LONG).show()
    }
    
    private fun stopNetworkSpeedOverlay() {
        val intent = Intent(this, NetworkSpeedOverlayService::class.java)
        stopService(intent)
        Toast.makeText(this, "Network Speed Overlay stopped", Toast.LENGTH_SHORT).show()
    }
    
    private fun resetPosition() {
        PositioningHelper.resetToDefaults(this)
        Toast.makeText(this, "Position reset to defaults. Restart overlay to apply.", Toast.LENGTH_SHORT).show()
    }
    
    companion object {
        private const val TAG = "MainActivity"
    }
}
