# Network Speed Overlay - Usage Guide

## 🎯 **New Features Added**

### **Runtime Positioning & Control**
- **Drag to Reposition**: Touch and drag the overlay to move it anywhere on screen
- **Remove Button**: Tap the red ✕ button to instantly remove the overlay
- **Position Memory**: Your positioning is automatically saved and restored
- **Reset Position**: Use the "Reset Position" button to restore default positioning

## 📱 **How to Use**

### **1. Starting the Overlay**
1. Open the "Network Speed Overlay" app
2. Grant system overlay permission if not already done
3. Tap "Start Network Speed Overlay"
4. The overlay will appear on the right side of your status bar

### **2. Repositioning the Overlay**
1. **Touch and drag** the overlay to move it anywhere on screen
2. The position is **automatically saved** when you release
3. Next time you start the overlay, it will appear in the same position

### **3. Removing the Overlay**
1. **Tap the red ✕ button** on the overlay to instantly remove it
2. Or use the "Stop Network Speed Overlay" button in the app

### **4. Resetting Position**
1. If you want to restore the default position (right side of status bar)
2. Tap "Reset Position" in the app
3. Restart the overlay to apply the reset

## 🎮 **Interactive Controls**

### **Drag Gesture**
- **Touch and hold** the overlay
- **Drag** to move it around the screen
- **Release** to save the new position
- Works anywhere on the overlay (not just the remove button)

### **Remove Button**
- **Red ✕ button** on the right side of the overlay
- **Single tap** to instantly remove the overlay
- **No confirmation needed** - immediate removal

## 🔧 **Positioning Tips**

### **Best Positions**
- **Status bar area**: Right side, just left of WiFi/mobile icons
- **Top of screen**: For easy visibility
- **Corner positions**: For minimal interference with other apps

### **Avoid These Areas**
- **Center of screen**: May interfere with app content
- **Bottom area**: May conflict with navigation gestures
- **Very edges**: May be hard to see or interact with

## 📊 **Display Format**

The overlay shows:
- **↓1.2MB/s ↑0.8MB/s** (Download and upload speeds)
- **WiFi** (Network type indicator)
- **✕** (Remove button)

## 🚀 **Quick Commands**

### **Build and Install**
```bash
./quick_build.sh
```

### **Reset Everything**
1. Stop overlay in app
2. Tap "Reset Position"
3. Restart overlay

### **Check Logs**
```bash
adb logcat -s NetworkSpeedOverlay
```

## 💡 **Pro Tips**

1. **Position Once**: Set your preferred position once, and it will be remembered
2. **Quick Remove**: Use the ✕ button for instant removal without opening the app
3. **Drag Sensitivity**: Move at least 10 pixels to trigger drag mode
4. **Position Memory**: Positions are saved per device, so each device remembers its own position

## 🐛 **Troubleshooting**

### **Overlay Not Draggable**
- Ensure system overlay permission is granted
- Try restarting the overlay

### **Position Not Saved**
- Check if the app has storage permissions
- Try the "Reset Position" button

### **Remove Button Not Working**
- Ensure the overlay is not in a restricted area
- Try stopping and restarting the overlay

## 🎉 **Enjoy Your Customizable Network Speed Monitor!**

The overlay is now fully interactive and customizable. Position it exactly where you want it, and it will remember your preferences!
