# Setup Instructions for Network Speed Overlay

## Prerequisites

### 1. Install Android Studio and SDK Tools

1. **Download Android Studio**:
   - Go to https://developer.android.com/studio
   - Download Android Studio for macOS
   - Install it following the setup wizard

2. **Install SDK Tools**:
   - Open Android Studio
   - Go to Tools → SDK Manager
   - Install Android SDK Platform-Tools (includes ADB)
   - Note the SDK location (usually `~/Library/Android/sdk`)

3. **Add ADB to PATH**:
   ```bash
   # Add this to your ~/.zshrc or ~/.bash_profile
   export PATH=$PATH:~/Library/Android/sdk/platform-tools
   
   # Reload your shell
   source ~/.zshrc
   ```

### 2. Enable Developer Options on Android Device

1. **Enable Developer Options**:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Developer options will appear in Settings

2. **Enable USB Debugging**:
   - Go to Settings → Developer Options
   - Enable "USB Debugging"
   - Enable "Install via USB" (if available)

3. **Connect Device**:
   - Connect your Android device via USB
   - Allow USB debugging when prompted on the device

### 3. Verify Device Connection

```bash
# Check if device is connected
adb devices

# You should see something like:
# List of devices attached
# ABC123456789    device
```

## Building and Installing the App

### Method 1: Using Android Studio (Recommended)

1. **Open Project**:
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the Network_Speed_Overlay folder
   - Click "Open"

2. **Build and Install**:
   - Click the "Run" button (green play icon)
   - Select your connected device
   - The app will build and install automatically

### Method 2: Using Command Line

```bash
# Navigate to project directory
cd /Users/hyperionsolitude/Downloads/Network_Speed_Overlay

# Build the app
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

## Testing the App

### 1. First Launch
1. Open the "Network Speed Overlay" app on your device
2. You'll see the main interface with permission buttons

### 2. Grant Permissions
1. **System Overlay Permission**:
   - Tap "Grant Permission" button
   - You'll be taken to Settings
   - Find "Network Speed Overlay" in the list
   - Enable "Allow display over other apps"

2. **Accessibility Service Permission** (Optional):
   - Tap "Grant Permission" button again
   - Go to Settings → Accessibility
   - Find "Network Speed Overlay"
   - Enable the service

### 3. Start the Overlay
1. Return to the app
2. Tap "Start Network Speed Overlay"
3. You should see a notification that the service is running
4. The network speed indicator should appear on your status bar

### 4. Verify Functionality
- The overlay should show download (↓) and upload (↑) speeds
- Network type should be displayed (WiFi/Mobile/Ethernet)
- Speed should update in real-time
- Overlay should be positioned on the status bar

## Troubleshooting

### Device Not Detected
```bash
# Check if ADB is working
adb version

# Restart ADB server
adb kill-server
adb start-server

# Check devices again
adb devices
```

### App Won't Install
1. Ensure USB debugging is enabled
2. Check if "Install via USB" is enabled in Developer Options
3. Try uninstalling any existing version first:
   ```bash
   adb uninstall com.networkspeed.overlay
   ```

### Overlay Not Showing
1. Check if system overlay permission is granted
2. Ensure the service is running (check notification panel)
3. Try restarting the app
4. Check if accessibility service is enabled (optional but recommended)

### Build Errors
1. Ensure you have the correct Android SDK version
2. Check if all dependencies are properly installed
3. Try cleaning and rebuilding:
   ```bash
   ./gradlew clean
   ./gradlew assembleDebug
   ```

## Alternative: Using Android Emulator

If you don't have a physical device:

1. **Create Virtual Device**:
   - Open Android Studio
   - Go to Tools → AVD Manager
   - Create a new virtual device
   - Choose a device definition and system image
   - Start the emulator

2. **Install App**:
   - The emulator will appear in the device list
   - Follow the same installation steps as above

## Next Steps

Once the app is installed and running:

1. **Test Network Speed Display**: Use different apps to generate network traffic and verify the speed updates
2. **Test Different Networks**: Try WiFi and mobile data to see different network type indicators
3. **Customize Position**: The overlay is positioned at the top-left, but you can modify the positioning in the code if needed

## Support

If you encounter any issues:
1. Check the Android Studio logcat for error messages
2. Ensure all permissions are properly granted
3. Verify your device meets the minimum requirements (Android 7.0+)
4. Try restarting both the app and your device
