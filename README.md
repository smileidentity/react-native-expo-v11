# Smile ID Expo SDK
<p align="center">
<a href="https://apps.apple.com/us/app/smile-id/id6448359701?itscg=30200&amp;itsct=apps_box_appicon" style="width: 170px; height: 170px; border-radius: 22%; overflow: hidden; display: inline-block; vertical-align: middle;"><img src="https://is1-ssl.mzstatic.com/image/thumb/Purple221/v4/30/4a/94/304a94c9-239c-e460-c7e0-702cc8945827/AppIcon-1x_U007emarketing-0-10-0-85-220-0.png/540x540bb.jpg" alt="Smile ID" style="width: 170px; height: 170px; border-radius: 22%; overflow: hidden; display: inline-block; vertical-align: middle;"></a>
</p>

![NPM Version](https://img.shields.io/npm/v/%40smile_identity%2Freact-native-expo)

Smile ID provides premier solutions for Real Time Digital KYC, Identity Verification, User Onboarding, and User Authentication across Africa.

If you haven’t already, [sign up](https://www.usesmileid.com/schedule-a-demo/) for a free Smile ID account, which comes with Sandbox access.

Please see [CHANGELOG.md](CHANGELOG.md) or [Releases](https://github.com/smileidentity/react-native/releases) for the most recent version and release notes.

<a href='https://play.google.com/store/apps/details?id=com.smileidentity.sample&utm_source=github&utm_campaign=reactnative&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="250" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

<a href="https://apps.apple.com/us/app/smile-id/id6448359701?itsct=apps_box_badge&amp;itscg=30200" style="display: inline-block; overflow: hidden; border-radius: 13px; width: 250px; height: 83px;"><img src="https://tools.applemediaservices.com/api/badges/download-on-the-app-store/black/en-us?size=250x83&amp;releaseDate=1710028800" alt="Download on the App Store" style="border-radius: 13px; width: 250px; height: 83px;"></a>

## Getting Started

Full documentation is available at [Smile ID Documentation](https://docs.usesmileid.com/integration-options/mobile/getting-started)

### 1. Requirements

* Node.js >=18.0
* Expo SDK 53
* React Native >=0.79
* A `smile_config.json` file from [Smile ID Portal](https://portal.usesmileid.com/sdk)
* See: [Android Requirements](https://github.com/smileidentity/android) for Android specific requirements.
* See: [iOS Requirements](https://github.com/smileidentity/ios) for iOS specific requirements.

### 2. Dependency

#### **For Expo Managed Projects**

Install the Smile ID Expo SDK:

   ```bash
   # Using npm
   npm install @smile_identity/react-native-expo

   # Using yarn
   yarn add @smile_identity/react-native-expo
   ```

##### Avoid "Cannot find native module 'SmileIDExpo'" in Expo projects

Expo Go does not bundle third‑party native modules. If you attempt to render any `SmileID*` native view inside Expo Go you'll see an error like:

```
 Cannot find native module 'SmileIDExpo', js engine: hermes
```

To fix this you must run a Development Build (a custom client) or a full release build that actually contains the native code.

In short: stop using Expo Go for any screen that renders Smile ID components; create a development build with `expo run:ios` or `expo run:android`; start (or let the run command start) the bundler using `expo start --dev-client`; and rebuild whenever you add, remove, or upgrade native dependencies (including `@smile_identity/react-native-expo`).

Step‑by‑step:

1. iOS Dev Build
   ```bash
   npx expo run:ios
   ```
   This prebuilds `ios/`, installs Pods, compiles a custom client and launches it.

2. Android Dev Build
   ```bash
   npx expo run:android
   ```


Version alignment: This SDK expects React Native and Metro versions that match (e.g. Expo SDK 53 ships RN 0.79.x + aligned Metro). If you override versions in `package.json`, ensure React Native and Metro remain compatible or you may see native view render failures.

Quick verification: After launching the dev build, navigate to a screen that uses a Smile ID component. If it renders without the native module error, the dev build is set up correctly.

###### Android target / compile SDK version requirement

If `npx expo run:android` fails with an error similar to:

```
Execution failed for task ':app:processDebugResources'.
Android resource linking failed
... AndroidManifest.xml: AAPT: error: attribute android:pageSizeCompat not found.
```

Your app is still building with the default Expo `compileSdkVersion/targetSdkVersion` (35), while the Smile ID Expo SDK requires 36. Update your Android build properties using the Expo Build Properties plugin:

1. Install the plugin (adds the compatible native Android versions):
    ```bash
    npx expo install expo-build-properties
    ```
2. Add (or update) the plugin entry in `app.json` / `app.config.js`:
    ```json
    {
       "expo": {
          "plugins": [
             [
                "expo-build-properties",
                {
                   "android": {
                      "compileSdkVersion": 36,
                      "targetSdkVersion": 36,
                      "buildToolsVersion": "36.0.0"
                   }
                }
             ]
          ]
       }
    }
    ```
3. Regenerate native Android project with a clean prebuild (ensures Gradle + manifest are recreated):
    ```bash
    npx expo prebuild -p android --clean
    ```
4. Run the Android dev build again:
    ```bash
    npx expo run:android
    ```

Whenever you adjust these Android version settings, repeat the clean prebuild + run sequence. This aligns the Expo managed workflow with the SDK's required Android API level and resolves the missing `android:pageSizeCompat` attribute error.

#### **For Bare React Native Projects**

1. **Install Expo Modules Support (if not already set up):**

   ```bash
   npx install-expo-modules@latest
   ```

   This will configure your bare React Native project with the required Gradle and Pod settings to support Expo modules.

2. **Install the Smile ID Expo SDK:**

   ```bash
    # Using npm
   npm install @smile_identity/react-native-expo
   
    # Using yarn
   yarn add @smile_identity/react-native-expo
   ```

3. **Rebuild Your Project:**

   ```bash
   cd sample-react-native/ios
   pod-install
   cd ..
    # For Android
   npx react-native run-android
   # or for iOS
   npx react-native run-ios
   ```
#### ⚠️ Important Note:
Ensure that your React Native and Metro versions match the versions used by **smile_identity/react-native-expo**.
You can override them to use different versions, but make sure both React Native and Metro are on the same version.
Mismatches can cause native views from **smile_identity/react-native-expo** to fail to render and throw runtime errors.

## 3. SDK Initialization

The Smile ID Expo SDK offers three flexible initialization methods to suit different development needs.

```typescript
import { initialize, SmileConfig } from 'react-native-expo';
```

### Option 1: Configuration File (Recommended)

This method uses a `smile_config.json` file containing your configuration settings.

**Setup Requirements:**
- **iOS**: Add `smile_config.json` to your copy bundle phases in your iOS target
- **Android**: Place `smile_config.json` in your assets folder

```typescript
// Initialize using configuration file
initialize(true);
```

### Option 2: Programmatic Configuration

```typescript
// Create configuration object with your Smile ID portal credentials
const config = new SmileConfig(
  'your_partner_id',              // Partner ID from Smile ID portal
  'your_auth_token',              // Authentication token
  'https://prod-lambda-url.com',  // Production lambda URL
  'https://test-lambda-url.com'   // Test lambda URL
);

// Initialize with configuration object
initialize(true, true, config);
```

### Option 3: Configuration with API Key

```typescript
// Use the same config object from Option 2
const config = new SmileConfig(
  'your_partner_id',
  'your_auth_token', 
  'https://prod-lambda-url.com',
  'https://test-lambda-url.com'
);

// Initialize with configuration and API key
initialize(true, true, config, 'YOUR_API_KEY');
```

## 4. Products

The SDK supports all [Smile ID products](https://docs.usesmileid.com/integration-options/mobile/products) with a simple, 
integrated component approach.**This implementation works the same way for both Expo projects and bare React Native 
projects.**

### Configuration Setup

First, create a document verification params object:

```typescript
import { DocumentVerificationParams } from 'react-native-expo';

const documentVerificationParams: DocumentVerificationParams = {
   userId: 'user123',
   jobId: 'job456',
   countryCode: 'NG',
   allowNewEnroll: false,
   documentType: 'PASSPORT',
   idAspectRatio: 1.414,
   bypassSelfieCaptureWithFile: 'your_selfie_image_path',
   enableAutoCapture: true,
   captureBothSides: false,
   allowAgentMode: true,
   showInstructions: true,
   showAttribution: true,
   allowGalleryUpload: true,
   skipApiSubmission: false,
   useStrictMode: false,
   extraPartnerParams: {
      'custom_param_1': 'value1',
      'custom_param_2': 'value2'
   }
};
```

### Implementation

Integrate the document verification component into your React Native view:

```typescript
import { SmileIDDocumentVerificationView } from 'react-native-expo';

// Component implementation
<SmileIDDocumentVerificationView 
  style={styles.nativeView}
  params={documentVerificationParams}
  onResult={handleDocumentVerificationResult}
  onError={handleDocumentVerificationError}
/>
```

### Event Handlers

Implement the required callback functions to handle verification results:

```typescript
// Handle successful verification
const handleSuccessResult = (result: DocumentVerificationResult) => {
  console.log('Document verification successful:', result);
  // Process the verification result
};

// Handle verification errors
const handleError = (error: DocumentVerificationError) => {
  console.error('Document verification failed:', error);
  // Handle error appropriately
};
```

**Component Props:**

* `style`: React Native StyleSheet for component styling
* `params`: Document verification parameters object
* `onResult`: Callback function for successful verification
* `onError`: Callback function for error handling

This implementation provides a complete document verification flow with comprehensive error handling and result processing capabilities.
Other Smile ID products can be integrated in a similar way using the provided components and configuration objects.

## Troubleshooting

Common build issues and their workarounds are documented in [TROUBLESHOOTING.md](TROUBLESHOOTING.md).

## Getting Help

For detailed documentation, please visit [Smile ID Documentation](https://docs.usesmileid.com/integration-options/mobile)

If you require further assistance, you can [file a support ticket](https://portal.usesmileid.com/partner/support/tickets) or [contact us](https://www.usesmileid.com/contact-us/)

## Contributing

Bug reports and Pull Requests are welcomed. Please see [CONTRIBUTING.md](CONTRIBUTING.md)

## License

[MIT License](LICENSE)
