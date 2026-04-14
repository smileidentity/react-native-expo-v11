import { Platform } from 'react-native';

/**
 * Detects if the app is running in Expo Go
 */
export function isExpoGo(): boolean {
  // Check for Expo Go specific constants
  const constants = Platform.constants as any;
  return (
    constants?.releaseChannel === 'default' &&
    constants?.executionEnvironment === 'StoreClient'
  );
}

/**
 * Detects if native modules are available
 */
export function isNativeModuleAvailable(moduleName: string): boolean {
  try {
    // Try to require the native module
    const NativeModules = require('react-native').NativeModules;
    return NativeModules && NativeModules[moduleName];
  } catch (error) {
    return false;
  }
}

/**
 * Detects if we're in a development environment
 */
export function isDevelopmentEnvironment(): boolean {
  return __DEV__;
}
