#!/bin/bash

# Network Speed Overlay - Position Adjustment Script
# This script helps you adjust the overlay position

echo "🔧 Network Speed Overlay - Position Adjustment"
echo "=============================================="

# Set up Android SDK path
export PATH=$PATH:/opt/homebrew/share/android-commandlinetools/platform-tools

# Check if device is connected
DEVICES=$(adb devices | grep -v "List of devices attached" | grep -v "^$" | wc -l)

if [ $DEVICES -eq 0 ]; then
    echo "❌ No Android devices connected."
    exit 1
fi

echo "📱 Device connected. Current positioning:"
echo "   X offset: 156 (distance from right edge)"
echo "   Y offset: 0 (vertical position)"
echo ""
echo "To adjust positioning, you can:"
echo "1. Use the app to stop and restart the overlay"
echo "2. Modify the PositioningHelper.kt file"
echo "3. Use ADB commands to adjust settings"
echo ""
echo "Common adjustments:"
echo "   Move left: Increase X offset (e.g., 200)"
echo "   Move right: Decrease X offset (e.g., 100)"
echo "   Move up: Decrease Y offset (e.g., -10)"
echo "   Move down: Increase Y offset (e.g., 10)"
echo ""
echo "To apply changes:"
echo "1. Stop the overlay in the app"
echo "2. Modify positioning in code"
echo "3. Rebuild and install: ./gradlew assembleDebug installDebug"
echo "4. Start the overlay again"
echo ""
echo "Current status:"
adb shell "dumpsys activity services | grep NetworkSpeedOverlay" || echo "Service not running"
