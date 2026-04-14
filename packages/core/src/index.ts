// Core types and interfaces
export { SmileConfig } from './types/SmileConfig';
export { 
  BiometricKYCParams,
  DocumentVerificationParams,
  SmartSelfieParams,
  ConsentParams,
  AutoCapture,
  SmileSensitivity,
  ConsentInformationParams,
  IdInfoParams,
  EnhancedDocumentVerificationParams
} from './types/SmileIDExpo.types';

export { default as SmileIDExpoModule } from './module/SmileIDExpoModule';

export { detectArchitecture, getArchitectureComponent } from './utils/ArchitectureDetector';
export { isExpoGo, isNativeModuleAvailable, isDevelopmentEnvironment } from './utils/ExpoGoDetector';

export * from './legacy';
