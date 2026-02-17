package com.smileidentity.react.expo

import com.smileidentity.models.AutoCapture
import com.smileidentity.models.Config
import com.smileidentity.models.ConsentInformation
import com.smileidentity.models.ConsentedInformation
import com.smileidentity.models.IdInfo
import com.smileidentity.models.SmileSensitivity
import expo.modules.kotlin.records.Record
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.types.Enumerable
import kotlinx.collections.immutable.toImmutableMap
import java.io.File
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Type‑safe bridge for the JS `SmileConfig` object coming from JavaScript.
 */
class SmileConfig : Record {
    @Field
    var partnerId: String = ""

    @Field
    var authToken: String = ""

    @Field
    var prodLambdaUrl: String = ""

    @Field
    var testLambdaUrl: String = ""
}

/**
 * Type‑safe bridge for the JS `DocumentVerificationParams` object
 */
class DocumentVerificationParams : Record {
    @Field
    var userId: String? = null

    @Field
    var jobId: String? = null

    @Field
    var countryCode: String = ""

    @Field
    var allowNewEnroll: Boolean = false

    @Field
    var documentType: String? = null

    @Field
    var idAspectRatio: Float? = null

    @Field
    var bypassSelfieCaptureWithFile: File? = null

    @Field
    var autoCaptureTimeout: Int = 10

    @Field
    var autoCapture: AutoCaptureParams  = AutoCaptureParams.AutoCapture

    @Field
    var captureBothSides: Boolean = true

    @Field
    var allowAgentMode: Boolean = false

    @Field
    var allowGalleryUpload: Boolean = false

    @Field
    var showInstructions: Boolean = true

    @Field
    var showAttribution: Boolean = true

    @Field
    var smileSensitivity: SmileSensitivityParams = SmileSensitivityParams.Normal

    @Field
    var skipApiSubmission: Boolean = false

    @Field
    var useStrictMode: Boolean = false

    @Field
    var extraPartnerParams: Map<String, String> = emptyMap()
}

/*
* Map the SmileConfig record to the SDK's expected Config data class
* */
internal fun SmileConfig.toConfig(): Config {
    return Config(
        partnerId = this.partnerId,
        authToken = this.authToken,
        prodLambdaUrl = this.prodLambdaUrl,
        testLambdaUrl = this.testLambdaUrl
    )
}

/*
* Map DocumentVerificationParams to DocumentVerificationProps
 */
internal fun DocumentVerificationParams.toDocumentVerificationProps(): DocumentVerificationProps {
    return DocumentVerificationProps(
        userId = this.userId,
        jobId = this.jobId,
        countryCode = this.countryCode,
        allowNewEnroll = this.allowNewEnroll,
        documentType = this.documentType,
        idAspectRatio = this.idAspectRatio,
        bypassSelfieCaptureWithFile = this.bypassSelfieCaptureWithFile,
        autoCaptureTimeout = this.autoCaptureTimeout,
        autoCapture = this.autoCapture.toAutoCapture(),
        captureBothSides = this.captureBothSides,
        allowAgentMode = this.allowAgentMode,
        allowGalleryUpload = this.allowGalleryUpload,
        showInstructions = this.showInstructions,
        showAttribution = this.showAttribution,
        skipApiSubmission = this.skipApiSubmission,
        smileSensitivity = this.smileSensitivity.toSmileSensitivity(),
        useStrictMode = this.useStrictMode,
        extraPartnerParams = this.extraPartnerParams.toImmutableMap()
    )
}

/**
 * Type‑safe bridge for the JS `EnhancedDocumentVerificationParams` object
 */
class EnhancedDocumentVerificationParams : Record {
    @Field
    var userId: String? = null

    @Field
    var jobId: String? = null

    @Field
    var countryCode: String = ""

    @Field
    var allowNewEnroll: Boolean = false

    @Field
    var documentType: String? = null

    @Field
    var idAspectRatio: Float? = null

    @Field
    var bypassSelfieCaptureWithFile: File? = null

    @Field
    var autoCaptureTimeout: Int = 10

    @Field
    var autoCapture: AutoCaptureParams = AutoCaptureParams.AutoCapture

    @Field
    var captureBothSides: Boolean = true

    @Field
    var allowAgentMode: Boolean = false

    @Field
    var allowGalleryUpload: Boolean = false

    @Field
    var showInstructions: Boolean = true

    @Field
    var showAttribution: Boolean = true

    @Field
    var skipApiSubmission: Boolean = false

    @Field
    var useStrictMode: Boolean = false

    @Field
    var extraPartnerParams: Map<String, String> = emptyMap()

    @Field
    var consentInformation: ConsentInformationParams? = null

}

/**
 * Type‑safe bridge for the JS `ConsentInformationParams` object
 */
class ConsentInformationParams : Record {
    @Field
    var consentGrantedDate: String =  getCurrentIsoTimestamp()
    @Field
    var personalDetails: Boolean = false
    @Field
    var contactInformation: Boolean = false
    @Field
    var documentInformation: Boolean = false
}

/**
 * Map EnhancedDocumentVerificationRequestRecord to DocumentVerificationProps
 */
internal fun EnhancedDocumentVerificationParams.toDocumentVerificationProps(): DocumentVerificationProps {
    return DocumentVerificationProps(
        userId = this.userId,
        jobId = this.jobId,
        countryCode = this.countryCode,
        allowNewEnroll = this.allowNewEnroll,
        documentType = this.documentType,
        idAspectRatio = this.idAspectRatio,
        bypassSelfieCaptureWithFile = this.bypassSelfieCaptureWithFile,
        autoCaptureTimeout = this.autoCaptureTimeout,
        autoCapture = this.autoCapture.toAutoCapture(),
        captureBothSides = this.captureBothSides,
        allowAgentMode = this.allowAgentMode,
        allowGalleryUpload = this.allowGalleryUpload,
        showInstructions = this.showInstructions,
        showAttribution = this.showAttribution,
        skipApiSubmission = this.skipApiSubmission,
        useStrictMode = this.useStrictMode,
        extraPartnerParams = this.extraPartnerParams.toImmutableMap(),
        consentInformation = this.consentInformation?.toConsentInformation()
    )
}

/**
 * Map ConsentInformationParams to ConsentInformation
 */

internal fun ConsentInformationParams.toConsentInformation(): ConsentInformation {
    return ConsentInformation(
        consented = ConsentedInformation(
            consentGrantedDate = this.consentGrantedDate,
            personalDetails = this.personalDetails,
            contactInformation = this.contactInformation,
            documentInformation = this.documentInformation
        )
    )
}

/**
 * Type‑safe bridge for the JS `SmartSelfieParams` object
 */
class SmartSelfieParams: Record {
    @Field
    var userId: String? = null

    @Field
    var jobId: String? = null

    @Field
    var allowNewEnroll: Boolean = true

    @Field
    var allowAgentMode: Boolean = false

    @Field
    var showAttribution: Boolean = true

    @Field
    var showInstructions: Boolean = true

    @Field
    var skipApiSubmission: Boolean = false

    @Field
    var useStrictMode: Boolean = false

    @Field
    var smileSensitivity: SmileSensitivityParams = SmileSensitivityParams.Normal

    @Field
    var extraPartnerParams: Map<String, String> = emptyMap()
}

/*
* Map SmartSelfieParams to SmartSelfieProps
 */
internal fun SmartSelfieParams.toSmartSelfieProps(): SmartSelfieProps {
    return SmartSelfieProps(
        userId = this.userId,
        jobId = this.jobId,
        allowNewEnroll = this.allowNewEnroll,
        allowAgentMode = this.allowAgentMode,
        showAttribution = this.showAttribution,
        showInstructions = this.showInstructions,
        skipApiSubmission = this.skipApiSubmission,
        useStrictMode = this.useStrictMode,
        smileSensitivity = this.smileSensitivity.toSmileSensitivity(),
        extraPartnerParams = this.extraPartnerParams.toImmutableMap()
    )
}

/**
 * Type‑safe bridge for the JS `BiometricKYCParams` object
 */
class BiometricKYCParams: Record {
    @Field
    var userId: String? = null

    @Field
    var jobId: String? = null

    @Field
    var allowNewEnroll: Boolean = true

    @Field
    var allowAgentMode: Boolean = false

    @Field
    var showAttribution: Boolean = true

    @Field
    var showInstructions: Boolean = true

    @Field
    var skipApiSubmission: Boolean = false

    @Field
    var smileSensitivity: SmileSensitivityParams = SmileSensitivityParams.Normal

    @Field
    var useStrictMode: Boolean = false

    @Field
    var consentInformation: ConsentInformationParams? = null

    @Field
    var idInfo: IdInfoParams? = null

    @Field
    var extraPartnerParams: Map<String, String> = emptyMap()
}

/*
* Type‑safe bridge for the JS `IdInfoParams` object
 */

class IdInfoParams: Record {
    @Field
    var country: String= ""
    @Field
    var idNumber: String? = null
    @Field
    var idType: String? = null
    @Field
    var firstName: String? = null
    @Field
    var middleName: String? = null
    @Field
    var lastName: String? = null
    @Field
    var dob: String? = null
    @Field
    var bankCode: String? = null
    @Field
    var entered: Boolean = false
}

/**
 * Map BiometricKYCParams to BiometricKYCProps
 */
internal fun BiometricKYCParams.toBiometricKYCProps(): BiometricKYCProps {
    return BiometricKYCProps(
        userId = this.userId,
        jobId = this.jobId,
        allowNewEnroll = this.allowNewEnroll,
        allowAgentMode = this.allowAgentMode,
        showAttribution = this.showAttribution,
        showInstructions = this.showInstructions,
        skipApiSubmission = this.skipApiSubmission,
        smileSensitivity = this.smileSensitivity.toSmileSensitivity(),
        useStrictMode = this.useStrictMode,
        consentInformation = this.consentInformation?.toConsentInformation(),
        extraPartnerParams = this.extraPartnerParams.toImmutableMap(),
        idInfo = this.idInfo.toIdInfo()
    )
}

/**
 * Map IdInfoParams to IdInfo
*/
internal fun IdInfoParams?.toIdInfo(): IdInfo {
    return IdInfo(
        country = this?.country ?: "",
        idNumber = this?.idNumber,
        idType = this?.idType,
        firstName = this?.firstName,
        middleName = this?.middleName,
        lastName = this?.lastName,
        dob = this?.dob,
        bankCode = this?.bankCode,
        entered = this?.entered ?: false
    )
}

/*
 * Enum for auto capture parameter
 */
enum class AutoCaptureParams(val value: String): Enumerable {
    AutoCapture("AutoCapture"),
    AutoCaptureOnly("AutoCaptureOnly"),
    ManualCaptureOnly("ManualCaptureOnly")
}

/**
 * Extension function to convert AutoCaptureParams to AutoCapture
 */
fun AutoCaptureParams.toAutoCapture(): AutoCapture =
    when (this) {
        AutoCaptureParams.AutoCapture -> AutoCapture.AutoCapture
        AutoCaptureParams.AutoCaptureOnly -> AutoCapture.AutoCaptureOnly
        AutoCaptureParams.ManualCaptureOnly -> AutoCapture.ManualCaptureOnly
    }

/*
 * Enum for smile sensitivity parameter
 */
enum class SmileSensitivityParams(val value: String): Enumerable {
    Normal("Normal"),
    Relaxed("Relaxed"),
}


/**
 * Extension function to convert SmileSensitivityParams to SmileSensitivity
 */
fun SmileSensitivityParams.toSmileSensitivity(): SmileSensitivity =
    when (this) {
        SmileSensitivityParams.Normal -> SmileSensitivity.NORMAL
        SmileSensitivityParams.Relaxed -> SmileSensitivity.RELAXED
    }
