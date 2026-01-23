import ExpoModulesCore
import SmileID
import SwiftUI
import UIKit

public class SmileIDExpoModule: Module {

    public func definition() -> ModuleDefinition {

        Name("SmileIDExpo")

       AsyncFunction("initialize") {
           (
               useSandBox: Bool,
               enableCrashReporting: Bool,
               config: SmileConfig?,
               apiKey: String?
           ) async throws -> Void in
				 SmileID.setWrapperInfo(name: .reactNativeExpo, version: "11.2.7")

           if let apiKey = apiKey, let config = config {
               // Case 1: Initialize with API key and config
              SmileID.initialize(
                apiKey: apiKey,
                config: Config(
                    partnerId: config.partnerId,
                    authToken: config.authToken,
                    prodLambdaUrl: config.prodLambdaUrl,
                    testLambdaUrl: config.testLambdaUrl
                ),
                useSandbox: useSandBox
               )
           } else if let config = config {
               // Case 2: Initialize with just config
               SmileID.initialize(
                   config: Config(
                       partnerId: config.partnerId,
                       authToken: config.authToken,
                       prodLambdaUrl: config.prodLambdaUrl,
                       testLambdaUrl: config.testLambdaUrl
                   ),
                   useSandbox: useSandBox
               )
           } else {
               // Case 3: Basic initialization
               SmileID.initialize(useSandbox: useSandBox)
           }
       }

        // Set callback url
        AsyncFunction("setCallbackUrl") { (callbackUrl: String) async throws -> Void in
            guard let url = URL(string: callbackUrl) else {
                   throw Exception(name: "InvalidUrl", description: "Invalid callback URL: \(callbackUrl)")
                 }
                 SmileID.setCallbackUrl(url: url)
        }

        // Set offline mode
        AsyncFunction("setAllowOfflineMode") { (allowOfflineMode: Bool) async throws -> Void in
            SmileID.setAllowOfflineMode(allowOfflineMode: allowOfflineMode)
        }

        // Submit a job
        AsyncFunction("submitJob") { (jobId: String) async throws -> Void in
            do {
                try  SmileID.submitJob(jobId: jobId)
            } catch {
                throw error
            }
        }

        // Get submitted jobs
        AsyncFunction("getSubmittedJobs") { () async throws -> [String] in
            let submittedJobs: [String] =  SmileID.getSubmittedJobs()
            return submittedJobs
        }

        // Get unsubmitted jobs
        AsyncFunction("getUnsubmittedJobs") { () async throws -> [String] in
            let unsubmittedJobs: [String] = SmileID.getUnsubmittedJobs()
                return unsubmittedJobs
        }

        //  Cleanup job data
        AsyncFunction("cleanup") { (jobId: String) async throws -> Void in
            do {
                try  SmileID.cleanup(jobId: jobId)
            } catch {
                throw error
            }
        }

        // Document Verification View
        View(SmileIDDocumentVerificationView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDDocumentVerificationView,
                config: DocumentVerificationParams
            ) in
                view.updateConfig(config)
            }
        }

        // Enhanced Document Verification View
        View(SmileIDDocumentVerificationEnhancedView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDDocumentVerificationEnhancedView,
                config: EnhancedDocumentVerificationParams
            ) in
                view.updateConfig(config)
            }
        }

        // SmartSelfie Enrollment View
        View(SmileIDSmartSelfieEnrollmentView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDSmartSelfieEnrollmentView,
                config: SmartSelfieParams
            ) in
                view.updateConfig(config)
            }
        }

        // Enhanced SmartSelfie Enrollment View
        View(SmileIDSmartSelfieEnrollmentEnhancedView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDSmartSelfieEnrollmentEnhancedView,
                config: SmartSelfieParams
            ) in
                view.updateConfig(config)
            }
        }

        // SmartSelfie Authentication View
        View(SmileIDSmartSelfieAuthenticationView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDSmartSelfieAuthenticationView,
                config: SmartSelfieParams
            ) in
                view.updateConfig(config)
            }
        }

        // Enhanced SmartSelfie Authentication View
        View(SmileIDSmartSelfieAuthenticationEnhancedView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDSmartSelfieAuthenticationEnhancedView,
                config: SmartSelfieParams
            ) in
                view.updateConfig(config)
            }
        }

        // Biometric KYC View
        View(SmileIDBiometricKYCView.self) {
            Events("onResult", "onError")

            Prop("params") { (
                view: SmileIDBiometricKYCView,
                config: BiometricKYCParams
            ) in
                view.updateConfig(config)
            }
        }

    }
}
