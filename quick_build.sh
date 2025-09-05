#!/bin/bash

# Quick build and install script for Network Speed Overlay
# This script sets up the environment and builds/installs the app

echo "🚀 Network Speed Overlay - Quick Build & Install"
echo "================================================"

# Set up Android SDK path
export PATH=$PATH:/opt/homebrew/share/android-commandlinetools/platform-tools

# Check if device is connected
echo "📱 Checking for connected devices..."
DEVICES=$(adb devices | grep -v "List of devices attached" | grep -v "^$" | wc -l)

if [ $DEVICES -eq 0 ]; then
    echo "❌ No Android devices connected."
    echo "   Please connect your device and enable USB debugging"
    exit 1
fi

echo "✅ Found $DEVICES connected device(s)"
adb devices

# Build and install
echo ""
echo "🔨 Building and installing..."
./gradlew clean assembleDebug installDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Success! App installed on your device."
    echo ""
    echo "Next steps:"
    echo "1. Open 'Network Speed Overlay' on your device"
    echo "2. Grant system overlay permission"
    echo "3. Optionally enable accessibility service"
    echo "4. Tap 'Start Network Speed Overlay'"
    echo ""
    echo "The network speed will appear on your status bar! 📊"
else
    echo "❌ Build/install failed. Check the error messages above."
fi
