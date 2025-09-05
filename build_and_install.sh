#!/bin/bash

# Network Speed Overlay - Build and Install Script
# This script helps build and install the Android app

echo "🚀 Network Speed Overlay - Build and Install Script"
echo "=================================================="

# Check if ADB is available
if ! command -v adb &> /dev/null; then
    echo "❌ ADB not found. Please install Android SDK Platform-Tools first."
    echo "   Add ~/Library/Android/sdk/platform-tools to your PATH"
    echo "   Or install Android Studio and add it to PATH"
    exit 1
fi

# Check if device is connected
echo "📱 Checking for connected devices..."
DEVICES=$(adb devices | grep -v "List of devices attached" | grep -v "^$" | wc -l)

if [ $DEVICES -eq 0 ]; then
    echo "❌ No Android devices connected."
    echo "   Please:"
    echo "   1. Connect your Android device via USB"
    echo "   2. Enable USB Debugging in Developer Options"
    echo "   3. Allow USB debugging when prompted"
    exit 1
fi

echo "✅ Found $DEVICES connected device(s)"
adb devices

# Build the project
echo ""
echo "🔨 Building the project..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

echo "✅ Build successful!"

# Install the app
echo ""
echo "📦 Installing the app..."
./gradlew installDebug
if [ $? -ne 0 ]; then
    echo "❌ Installation failed. Please check the error messages above."
    exit 1
fi

echo "✅ Installation successful!"

# Show next steps
echo ""
echo "🎉 App installed successfully!"
echo ""
echo "Next steps:"
echo "1. Open the 'Network Speed Overlay' app on your device"
echo "2. Grant system overlay permission when prompted"
echo "3. Optionally enable accessibility service for better positioning"
echo "4. Tap 'Start Network Speed Overlay' to begin monitoring"
echo ""
echo "The network speed will appear on your status bar showing:"
echo "  ↓ Download speed ↑ Upload speed"
echo "  Network type (WiFi/Mobile/Ethernet)"
echo ""
echo "Happy monitoring! 📊"
