package com.smileidentity.react.expo

import androidx.core.net.toUri
import com.smileidentity.SmileID
import expo.modules.kotlin.functions.Coroutine
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.smileidentity.metadata.models.WrapperSdkName
import expo.modules.kotlin.exception.CodedException
import java.net.URL

class SmileIDExpoModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("SmileIDExpo")

        // functions
        AsyncFunction("initialize") Coroutine { useSandBox: Boolean, enableCrashReporting: Boolean, config: SmileConfig?, apiKey: String? ->
            val context = appContext.reactContext
                ?: throw IllegalStateException("Context is not available")

            withContext(Dispatchers.IO) {
                SmileID.setWrapperInfo(WrapperSdkName.ReactNativeExpo, "11.2.7")

                when {
                    // Case 1: Initialize with API key and config
                    apiKey != null && config != null -> {

                        val result = SmileID.initialize(
                            context = context,
                            config = config.toConfig(),
                            useSandbox = useSandBox,
                            enableCrashReporting = enableCrashReporting,
                            apiKey = apiKey
                        ).await()

                        result.getOrThrow()
                    }
                    // Case 2: Initialize with just config
                    config != null -> {
                        val result = SmileID.initialize(
                            context = context,
                            config = config.toConfig(),
                            useSandbox = useSandBox,
                            enableCrashReporting = enableCrashReporting,
                        ).await()

                        result.getOrThrow()
                    }
                    // Case 3: Basic initialization
                    else -> {
                        val result = SmileID.initialize(
                            context = context,
                            useSandbox = useSandBox,
                        ).await()
                        result.getOrThrow()
                    }
                }
            }
        }

        AsyncFunction("setCallbackUrl") { callbackUrl : String ->
            return@AsyncFunction SmileID.setCallbackUrl(URL(callbackUrl))

        }
        AsyncFunction("setAllowOfflineMode") { allowOfflineMode : Boolean ->
            return@AsyncFunction SmileID.setAllowOfflineMode(allowOfflineMode)
        }
        AsyncFunction("submitJob") { jobId : String ->
           try {
               val result = SmileID.submitJob(jobId)
               return@AsyncFunction result
           } catch (e: Exception) {
               throw CodedException(
                   "SubmitJobError",
                   "Failed to submit job: ${e.message}",
                   e
               )
           }
        }
        AsyncFunction("getSubmittedJobs") {
            try {
                val jobs = SmileID.getSubmittedJobs()
                return@AsyncFunction jobs
            } catch (e: Exception) {
                throw CodedException(
                    "GetSubmittedJobsError",
                    "Failed to get submitted jobs: ${e.message}",
                    e
                )
            }
        }

        AsyncFunction("getUnsubmittedJobs") {
            try {
                val jobs = SmileID.getUnsubmittedJobs()
                 return@AsyncFunction jobs
            } catch (e: Exception) {
                throw CodedException(
                    "GetUnsubmittedJobsError",
                    "Failed to get unsubmitted jobs: ${e.message}",
                    e
                )
            }
        }
        AsyncFunction("cleanup") { jobId : String ->
            try {
                val result  = SmileID.cleanup(jobId)
                return@AsyncFunction result
            } catch (e: Exception) {
                throw CodedException(
                    "CleanupError",
                    "Failed to cleanup job: ${e.message}",
                    e
                )
            }
        }

        // Views
        View(SmileIDDocumentVerificationView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDDocumentVerificationView, config: DocumentVerificationParams ->
                view.updateConfig(config)
            }
        }

        View(SmileIDDocumentVerificationEnhancedView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDDocumentVerificationEnhancedView, config: EnhancedDocumentVerificationParams ->
                view.updateConfig(config)
            }
        }

        View(SmileIDSmartSelfieEnrollmentView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDSmartSelfieEnrollmentView, config: SmartSelfieParams ->
                view.updateConfig(config)
            }
        }

        View(SmileIDSmartSelfieEnrollmentEnhancedView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDSmartSelfieEnrollmentEnhancedView, config: SmartSelfieParams ->
                view.updateConfig(config)
            }
        }

        View(SmileIDSmartSelfieAuthenticationView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDSmartSelfieAuthenticationView, config: SmartSelfieParams ->
                view.updateConfig(config)
            }
        }

        View(SmileIDSmartSelfieAuthenticationEnhancedView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDSmartSelfieAuthenticationEnhancedView, config: SmartSelfieParams ->
                view.updateConfig(config)
            }
        }

        View(SmileIDBiometricKYCView::class) {
            Events("onResult", "onError")
            Prop("params") { view: SmileIDBiometricKYCView, config: BiometricKYCParams ->
                view.updateConfig(config)
            }
        }
    }
}
