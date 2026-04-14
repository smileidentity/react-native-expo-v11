import { Platform } from 'react-native';

export interface ArchitectureInfo {
  isNewArchitecture: boolean;
  isFabricEnabled: boolean;
  isTurboModuleEnabled: boolean;
  platform: 'ios' | 'android' | 'web';
}

/**
 * Detects if the app is running with React Native's New Architecture
 */
export function detectArchitecture(): ArchitectureInfo {
  const platform = Platform.OS as 'ios' | 'android' | 'web';
  
  // Check if New Architecture is enabled
  const isNewArchitecture = (typeof globalThis !== 'undefined' && (globalThis as any).__turboModuleProxy !== undefined);
  
  // Fabric is typically enabled with New Architecture
  const isFabricEnabled = isNewArchitecture;
  
  // TurboModule detection
  const isTurboModuleEnabled = isNewArchitecture;
  
  return {
    isNewArchitecture,
    isFabricEnabled,
    isTurboModuleEnabled,
    platform
  };
}

/**
 * Returns the appropriate component based on architecture
 */
export function getArchitectureComponent<T>(
  fabricComponent: T,
  legacyComponent: T
): T {
  const { isNewArchitecture } = detectArchitecture();
  return isNewArchitecture ? fabricComponent : legacyComponent;
}
