import { TurboModule, TurboModuleRegistry } from 'react-native';

// TypeScript interface for TurboModule methods
export interface Spec extends TurboModule {
  initialize(
    useSandBox: boolean,
    enableCrashReporting: boolean,
    config?: any,
    apiKey?: string,
  ): Promise<void>;
  
  setCallbackUrl(callbackUrl: string): Promise<void>;
  setAllowOfflineMode(allowOfflineMode: boolean): Promise<void>;
  submitJob(jobId: string): Promise<void>;
  getSubmittedJobs(): Promise<[string]>;
  getUnsubmittedJobs(): Promise<[string]>;
  cleanup(jobId: string): Promise<void>;
}

// Register TurboModule
export default TurboModuleRegistry.get<Spec>('SmileIDTurboModule') as Spec | null;
