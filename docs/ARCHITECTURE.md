# Architecture Documentation

## Overview

Network Speed Overlay is built using modern Android development practices with a focus on performance, efficiency, and maintainability.

## Architecture Pattern

The app follows a **Service-based Architecture** with the following components:

### Core Components

#### 1. MainActivity
- **Purpose**: Entry point and user interface
- **Responsibilities**:
  - Permission management
  - Service lifecycle control
  - User interaction handling
  - UI state management

#### 2. NetworkSpeedOverlayService
- **Purpose**: Core service for network monitoring and overlay management
- **Responsibilities**:
  - Network speed calculation
  - Overlay display and positioning
  - Touch event handling
  - Performance monitoring

#### 3. PositioningHelper
- **Purpose**: Position management and persistence
- **Responsibilities**:
  - Save/restore overlay position
  - Default position handling
  - Position validation

#### 4. NetworkSpeedAccessibilityService
- **Purpose**: Accessibility service for enhanced positioning
- **Responsibilities**:
  - System UI interaction
  - Enhanced positioning capabilities
  - Accessibility features

## Data Flow

```
User Interaction → MainActivity → Service → Overlay → UI Update
                     ↓
              Permission Check
                     ↓
              Position Management
```

## Key Design Decisions

### 1. Service-based Architecture
- **Reason**: Continuous background monitoring requires a foreground service
- **Benefits**: Reliable operation, system integration, proper lifecycle management

### 2. System Overlay
- **Reason**: Display over other apps requires system overlay permission
- **Implementation**: `TYPE_APPLICATION_OVERLAY` with proper flags

### 3. Smart Update System
- **Reason**: Reduce battery usage and improve performance
- **Implementation**: Only update UI when significant changes occur

### 4. Position Persistence
- **Reason**: User experience - remember preferred position
- **Implementation**: SharedPreferences for simple, reliable storage

## Performance Optimizations

### Memory Management
- AtomicBoolean for thread-safe state management
- Proper resource cleanup in onDestroy()
- Hardware acceleration for smooth rendering

### CPU Optimization
- Smart update throttling
- Efficient network speed calculation
- Background thread for monitoring

### Battery Optimization
- Minimal background processing
- Intelligent update frequency
- Proper service lifecycle management

## Security Considerations

### Permissions
- `SYSTEM_ALERT_WINDOW`: Required for overlay display
- `FOREGROUND_SERVICE`: Required for background monitoring
- `ACCESS_NETWORK_STATE`: Required for network type detection
- `ACCESS_WIFI_STATE`: Required for WiFi state monitoring
- `INTERNET`: Required for network access

### Data Privacy
- No data collection or transmission
- Local storage only
- No user data logging

## Error Handling

### Service Recovery
- Automatic restart on crash
- Graceful degradation
- State restoration

### Overlay Management
- Safe view operations
- Error logging
- Fallback positioning

### Network Monitoring
- Handle network state changes
- Graceful error recovery
- Validation of network data

## Testing Strategy

### Unit Tests
- Utility functions
- Position management
- Network speed calculation

### Integration Tests
- Service lifecycle
- Overlay positioning
- Permission handling

### UI Tests
- Main activity interactions
- Overlay dragging
- Permission flows

## Future Enhancements

### Planned Features
- Light theme option
- Custom color schemes
- Network usage statistics
- Widget support

### Architecture Improvements
- MVVM pattern implementation
- Repository pattern for data
- Dependency injection
- Modular architecture

## Dependencies

### Core Dependencies
- `androidx.core:core-ktx` - Core Android extensions
- `androidx.appcompat:appcompat` - AppCompat library

### Build Dependencies
- `com.android.tools.build:gradle` - Android Gradle Plugin
- `org.jetbrains.kotlin:kotlin-gradle-plugin` - Kotlin support

## Build Configuration

### Gradle Setup
- R8 code shrinking enabled
- Resource shrinking enabled
- ProGuard optimization
- APK splitting by architecture

### ProGuard Rules
- Keep essential classes
- Remove unused code
- Optimize performance
- Maintain functionality

## Deployment

### Release Process
1. Update version numbers
2. Run full test suite
3. Build release APK
4. Sign with release key
5. Upload to GitHub Releases
6. Update documentation

### Version Management
- Semantic versioning (MAJOR.MINOR.PATCH)
- Changelog maintenance
- Release notes
- Tagged releases
