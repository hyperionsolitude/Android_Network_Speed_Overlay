package com.networkspeed.overlay

import android.content.Context
import android.content.SharedPreferences

object PositioningHelper {
    private const val PREFS_NAME = "network_speed_overlay_prefs"
    private const val KEY_X_OFFSET = "x_offset"
    private const val KEY_Y_OFFSET = "y_offset"
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun getXOffset(context: Context): Int {
        return getPrefs(context).getInt(KEY_X_OFFSET, 156) // Default: 16 + 120 + 20
    }
    
    fun setXOffset(context: Context, offset: Int) {
        getPrefs(context).edit().putInt(KEY_X_OFFSET, offset).apply()
    }
    
    fun getYOffset(context: Context): Int {
        return getPrefs(context).getInt(KEY_Y_OFFSET, 0) // Default: middle of status bar
    }
    
    fun setYOffset(context: Context, offset: Int) {
        getPrefs(context).edit().putInt(KEY_Y_OFFSET, offset).apply()
    }
    
    fun resetToDefaults(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
