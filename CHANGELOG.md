# Release Notes

## 11.2.8 - February 17, 2026

### Fixed
* Fix `didSubmitBiometricJob` not being returned from Android Biometric KYC Job.
* Fix `extraPartnerParams` not being properly propagated on Android.

## 11.2.7 - January 23, 2026

### Changed
* Bump Android SDK to [v11.1.7](https://github.com/smileidentity/android/releases/tag/v11.1.7)
* Bump iOS SDK to [v11.1.7](https://github.com/smileidentity/ios/releases/tag/v11.1.7)

## 11.2.1 - December 23, 2025

### Changed
* Renamed `selfie` return type on `SmileIDDocumentVerificationView` and  `SmileIDDocumentVerificationEnhancedView` to
 `selfieFile` to match Android and the docs. 
* Extracted result mapping logic on Android to separate extension functions.
* Bump Android SDK to [v11.1.6](https://github.com/smileidentity/android/releases/tag/v11.1.6)
* Bump iOS SDK to [v11.1.5](https://github.com/smileidentity/ios/releases/tag/v11.1.5)

### Added
* Added smile sensitivity parameter for customizable selfie capture thresholds.
* Added smile sensitivity parameter to biometric and document verification.

### Fixed
* Added workaround for iOS Swift module interface verification error (`SwiftVerifyEmittedModuleInterface`) that may occur with certain Xcode versions

## 11.2.0 - October 7, 2025

### 🚀 Major Updates
* **Upgraded Expo SDK from 53 to 54** - Full compatibility with the latest Expo release
    * Updated React and React Native to the latest compatible versions
    * Updated Kotlin to version 2.1.20 and removed dependencies pinned to Kotlin 2.0.21 for Expo SDK 54 compatibility
    * Upgraded Android Gradle Plugin (AGP) to version 8.13

### Changed
* **Android SDK**: Bumped to [v11.1.2](https://github.com/smileidentity/android/releases/tag/v11.1.2)
* **sample-react-native**: Updated Metro and package configuration to use a local SDK source, fixed iOS entry point, and resolved TypeScript type and path issues

### Added
* Added Dependabot for automatic dependency updates.

## 11.1.1 - August 26, 2025

### Changed
* Bump iOS to 11.1.1 (https://github.com/smileidentity/ios/releases/tag/v11.1.1)

## 11.1.0 - August 5, 2025

### Changed
* Changed `enableAutoCapture` to `AutoCapture` enum to allow partners change document capture options
* Upgraded Smile ID Android and iOS SDKs to version **v11.1.0**.
* Bumped `compileSdkVersion` to 36 and resolved related build issues.

### Added
* Added `autoCaptureTimeout` to allow partners to configure the auto-capture timeout duration.

### Removed
* Removed the default `ConsentInformation`

## 11.0.0 - July 31, 2025

* Initial Release
