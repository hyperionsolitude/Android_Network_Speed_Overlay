# Contributing to Network Speed Overlay

Thank you for your interest in contributing to Network Speed Overlay! We welcome contributions from the community and appreciate your help in making this project better.

## 🤝 How to Contribute

### Reporting Issues
- Use the [GitHub Issues](https://github.com/yourusername/Network_Speed_Overlay/issues) page
- Search existing issues before creating a new one
- Provide detailed information about the problem
- Include device information and Android version
- Add screenshots if applicable

### Suggesting Features
- Use the [GitHub Discussions](https://github.com/yourusername/Network_Speed_Overlay/discussions) page
- Check existing feature requests first
- Provide a clear description of the proposed feature
- Explain the use case and benefits

### Code Contributions
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests if applicable
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

## 🛠️ Development Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 34
- Kotlin 1.8
- Gradle 8.0+
- Git

### Setup Steps
1. Fork and clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Build and run the project
5. Make your changes
6. Test thoroughly

### Code Style Guidelines

#### Kotlin
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public functions
- Use `val` instead of `var` when possible
- Prefer immutable data structures

#### Example:
```kotlin
/**
 * Updates the network speed overlay with new values
 * @param downloadSpeed Download speed in bytes per second
 * @param uploadSpeed Upload speed in bytes per second
 * @param networkType Type of network connection
 */
private fun updateOverlay(downloadSpeed: Long, uploadSpeed: Long, networkType: String) {
    // Implementation
}
```

#### Android
- Follow [Android Code Style Guidelines](https://source.android.com/setup/contribute/code-style)
- Use ViewBinding instead of findViewById
- Implement proper lifecycle management
- Handle configuration changes appropriately
- Use appropriate Android APIs for the target SDK

#### Performance
- Minimize object allocations
- Use efficient data structures
- Implement proper memory management
- Avoid memory leaks
- Use background threads for heavy operations

## 📋 Pull Request Guidelines

### Before Submitting
- [ ] Code follows the project's style guidelines
- [ ] Self-review of your code
- [ ] Code is properly commented
- [ ] Tests pass (if applicable)
- [ ] No new warnings introduced
- [ ] Documentation updated (if needed)

### PR Description
- Clear title describing the change
- Detailed description of what was changed
- Reference any related issues
- Include screenshots for UI changes
- List any breaking changes

### Example PR Title:
```
feat: Add light theme option for overlay
fix: Resolve memory leak in service cleanup
perf: Optimize network speed calculation algorithm
docs: Update README with new installation steps
```

## 🧪 Testing

### Manual Testing
- Test on different Android versions (7.0+)
- Test on different screen sizes
- Test with different network types (WiFi, Mobile, Ethernet)
- Test overlay positioning and dragging
- Test permission handling
- Test app lifecycle (background/foreground)

### Automated Testing
- Unit tests for utility functions
- Integration tests for service functionality
- UI tests for main activity
- Performance tests for memory usage

### Test Checklist
- [ ] App starts without crashes
- [ ] Overlay appears and functions correctly
- [ ] Dragging works smoothly
- [ ] Position is saved and restored
- [ ] Network speed updates correctly
- [ ] Dark theme displays properly
- [ ] Permissions are handled gracefully
- [ ] App works in background
- [ ] Memory usage is reasonable

## 📝 Documentation

### Code Documentation
- Add KDoc comments for public functions
- Document complex algorithms
- Explain non-obvious code sections
- Update README for new features

### User Documentation
- Update README.md for new features
- Add screenshots for UI changes
- Update installation instructions
- Document any new requirements

## 🐛 Bug Reports

### Required Information
- Android version
- Device model
- App version
- Steps to reproduce
- Expected behavior
- Actual behavior
- Screenshots (if applicable)
- Logcat output (if relevant)

### Bug Report Template
```markdown
**Bug Description**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected Behavior**
A clear description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Device Information**
- Device: [e.g. Samsung Galaxy S21]
- OS: [e.g. Android 12]
- App Version: [e.g. 1.2.0]

**Additional Context**
Add any other context about the problem here.
```

## 🚀 Feature Requests

### Feature Request Template
```markdown
**Feature Description**
A clear description of the feature you'd like to see.

**Use Case**
Describe the problem this feature would solve.

**Proposed Solution**
A clear description of what you want to happen.

**Alternatives**
Describe any alternative solutions you've considered.

**Additional Context**
Add any other context or screenshots about the feature request here.
```

## 📞 Getting Help

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For general questions and discussions
- **Email**: your.email@example.com for private matters

## 🏆 Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- GitHub contributors page

## 📄 License

By contributing to Network Speed Overlay, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to Network Speed Overlay! 🎉
