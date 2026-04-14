import { NativeModule, requireNativeModule } from "expo";
import { SmileConfig } from "../types/SmileConfig";
import { SmileIDExpoModuleEvents } from "../types/SmileIDExpo.types";

declare class SmileIDExpoModule extends NativeModule<SmileIDExpoModuleEvents> {
  /**
   * Initialize SmileID SDK with configuration
   */
  initialize(
      useSandBox: boolean,
      enableCrashReporting: boolean,
      config?: SmileConfig,
      apiKey?: string,
  ): Promise<void>;

  /**
   * The callback mechanism allows for asynchronous job requests and responses
   */
  setCallbackUrl: (callbackUrl: string) => Promise<void>;

  /**
   * Sets allow offline mode which enables the ability to capture jobs offline and submit later
   */
  setAllowOfflineMode: (allowOfflineMode: boolean) => Promise<void>;

  /**
   * Submits an already captured job id
   */
  submitJob: (jobId: string) => Promise<void>;

  /**
   * Returns all job ids from the submitted directory
   */
  getSubmittedJobs: () => Promise<[string]>;

  /**
   * Returns all job ids from the unsubmitted directory
   */
  getUnsubmittedJobs: () => Promise<[string]>;

  /**
   * Cleans up a job id from the submitted or unsubmitted directory
   */
  cleanup: (jobId: string) => Promise<void>;
}

export default requireNativeModule<SmileIDExpoModule>("SmileIDExpo");
