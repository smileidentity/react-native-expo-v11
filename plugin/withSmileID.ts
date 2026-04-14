import { ConfigPlugin, withAndroidStyles, withInfoPlist, withAppBuildGradle, withProjectBuildGradle } from '@expo/config-plugins';
import { ExpoConfig } from '@expo/config-types';

// Type definitions for plugin props
interface SmileIDPluginProps {
  smileConfigPath?: string;
  androidSdkVersion?: number;
  iosPermissions?: {
    camera?: boolean;
    microphone?: boolean;
    photoLibrary?: boolean;
  };
}

const DEFAULT_ANDROID_SDK_VERSION = 36;

export const withSmileID: ConfigPlugin<SmileIDPluginProps> = (config: ExpoConfig, props: SmileIDPluginProps = {}) => {
  const {
    smileConfigPath,
    androidSdkVersion = DEFAULT_ANDROID_SDK_VERSION,
    iosPermissions = { camera: true, microphone: true, photoLibrary: true }
  } = props;

  // Android SDK Version Injection
  config = withProjectBuildGradle(config, (newConfig) => {
    if (newConfig.modResults.language === 'groovy') {
      const buildGradle = newConfig.modResults.contents;
      
      // Add Android SDK versions if not present
      if (!buildGradle.includes('compileSdkVersion')) {
        newConfig.modResults.contents = buildGradle.replace(
          /android\s*\{/,
          `android {\n    compileSdkVersion ${androidSdkVersion}\n    buildToolsVersion "${androidSdkVersion}.0.0"\n    defaultConfig {\n        targetSdkVersion ${androidSdkVersion}\n        minSdkVersion 21\n    }`
        );
      }
    }
    return newConfig;
  });

  // iOS Permissions Injection
  config = withInfoPlist(config, (newConfig) => {
    const plist = newConfig.modResults;
    
    if (iosPermissions.camera && !plist.NSCameraUsageDescription) {
      plist.NSCameraUsageDescription = 'Smile ID needs camera access for identity verification';
    }
    
    if (iosPermissions.microphone && !plist.NSMicrophoneUsageDescription) {
      plist.NSMicrophoneUsageDescription = 'Smile ID needs microphone access for voice verification';
    }
    
    if (iosPermissions.photoLibrary && !plist.NSPhotoLibraryUsageDescription) {
      plist.NSPhotoLibraryUsageDescription = 'Smile ID needs photo library access for document verification';
    }
    
    return newConfig;
  });

  // Android Styles for Camera Preview
  config = withAndroidStyles(config, (newConfig) => {
    const styles = newConfig.modResults;
    
    // Add theme for camera preview
    if (!styles.resources.style?.find(s => s.$.name === 'SmileIDCameraTheme')) {
      styles.resources.style = styles.resources.style || [];
      styles.resources.style.push({
        $: {
          name: 'SmileIDCameraTheme',
          parent: 'Theme.AppCompat.Light.NoActionBar'
        },
        item: [
          { $: { name: 'windowNoTitle' }, _: 'true' },
          { $: { name: 'windowActionBar' }, _: 'false' },
          { $: { name: 'android:windowFullscreen' }, _: 'true' },
          { $: { name: 'android:windowContentOverlay' }, _: '@null' }
        ]
      });
    }
    
    return newConfig;
  });

  // Handle smile_config.json placement
  if (smileConfigPath) {
    config = withAppBuildGradle(config, (newConfig) => {
      if (newConfig.modResults.language === 'groovy') {
        const buildGradle = newConfig.modResults.contents;
        
        // Add smile_config.json to assets
        if (!buildGradle.includes('smile_config.json')) {
          newConfig.modResults.contents = buildGradle.replace(
            /android\s*\{/,
            `android {\n    sourceSets {\n        main {\n            assets.srcDirs += ['${smileConfigPath}']\n        }\n    }`
          );
        }
      }
      return newConfig;
    });
  }

  return config;
};

export default withSmileID;
