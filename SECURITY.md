# Security Policy

## Supported Versions

Use this section to tell people about which versions of your project are
currently being supported with security updates.

| Version | Supported          |
| ------- | ------------------ |
| 1.2.x   | :white_check_mark: |
| 1.1.x   | :white_check_mark: |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take security vulnerabilities seriously. If you discover a security vulnerability, please report it to us as described below.

### How to Report

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please report them via one of the following methods:

1. **Email**: Send details to security@example.com
2. **GitHub Security Advisory**: Use GitHub's private vulnerability reporting feature
3. **Private Message**: Contact maintainers directly

### What to Include

Please include the following information in your report:

- Description of the vulnerability
- Steps to reproduce the issue
- Potential impact
- Suggested fix (if any)
- Your contact information (optional)

### Response Timeline

- **Acknowledgment**: Within 48 hours
- **Initial Assessment**: Within 7 days
- **Resolution**: Within 30 days (depending on severity)

### Security Considerations

#### Permissions
This app requires the following permissions:
- `SYSTEM_ALERT_WINDOW`: Required for overlay display
- `FOREGROUND_SERVICE`: Required for background monitoring
- `ACCESS_NETWORK_STATE`: Required for network type detection
- `ACCESS_WIFI_STATE`: Required for WiFi state monitoring
- `INTERNET`: Required for network access

#### Data Privacy
- **No Data Collection**: The app does not collect, store, or transmit any personal data
- **Local Storage Only**: All data is stored locally on the device
- **No Network Communication**: The app does not communicate with external servers
- **No User Tracking**: No analytics or tracking mechanisms

#### Security Measures
- **Code Obfuscation**: Release builds use ProGuard/R8 obfuscation
- **Input Validation**: All user inputs are validated
- **Error Handling**: Comprehensive error handling prevents crashes
- **Memory Management**: Proper resource cleanup prevents memory leaks

#### Vulnerability Types

We are particularly interested in reports about:

- **Permission Escalation**: Unauthorized access to system permissions
- **Memory Corruption**: Buffer overflows or memory leaks
- **Code Injection**: Remote code execution vulnerabilities
- **Data Exposure**: Unintended data access or logging
- **Denial of Service**: App crashes or system instability
- **Privacy Violations**: Unauthorized data collection or transmission

#### Out of Scope

The following are considered out of scope for security reports:

- **UI/UX Issues**: Cosmetic problems or usability issues
- **Performance Issues**: Slow performance or high resource usage
- **Feature Requests**: Requests for new functionality
- **Third-party Libraries**: Vulnerabilities in dependencies (report to respective maintainers)

## Security Best Practices

### For Users
- Keep your Android device updated
- Only install apps from trusted sources
- Review app permissions before granting
- Use device security features (screen lock, etc.)

### For Developers
- Follow secure coding practices
- Regular security audits
- Keep dependencies updated
- Use proper error handling
- Implement input validation

## Disclosure Policy

When we receive a security bug report, we will:

1. Confirm the issue and determine affected versions
2. Develop a fix for the latest version
3. Test the fix thoroughly
4. Release the fix in a new version
5. Publicly disclose the vulnerability (if appropriate)

## Security Updates

Security updates will be released as:
- **Patch releases** (e.g., 1.2.1) for critical security fixes
- **Minor releases** (e.g., 1.3.0) for security improvements
- **Major releases** (e.g., 2.0.0) for significant security changes

## Contact

For security-related questions or concerns:
- **Email**: security@example.com
- **GitHub**: Use private vulnerability reporting
- **Response Time**: Within 48 hours

## Acknowledgments

We appreciate the security research community and welcome responsible disclosure of vulnerabilities. Contributors who report valid security issues will be acknowledged in our security advisories (unless they prefer to remain anonymous).

## License

This security policy is part of the project and is subject to the same license terms as the main project.
