import ExpoModulesCore
import SmileID
/// Type‑safe bridge for the JS `SmileConfig` object
struct SmileConfig: Record {
    @Field public var partnerId: String
    @Field public var authToken: String
    @Field public var prodLambdaUrl: String
    @Field public var testLambdaUrl: String
}

/// Type‑safe bridge for the JS `DocumentVerificationParams` object
struct DocumentVerificationParams: Record {
    @Field public var userId: String?
    @Field public var jobId: String?
    @Field public var countryCode: String
    @Field public var allowNewEnroll: Bool = false
    @Field public var documentType: String?
    @Field public var idAspectRatio: Double?
    @Field public var bypassSelfieCaptureWithFile: String?
    @Field public var autoCaptureTimeout: Int = 10
    @Field public var autoCapture: AutoCaptureParams = .autoCapture
    @Field public var captureBothSides: Bool = true
    @Field public var allowAgentMode: Bool = false
    @Field public var forceAgentMode: Bool = false
    @Field public var allowGalleryUpload: Bool = false
    @Field public var showInstructions: Bool = true
    @Field public var showAttribution: Bool = true
    @Field public var smileSensitivity: SmileSensitivityParams = .normal
    @Field public var skipApiSubmission: Bool = false
    @Field public var useStrictMode: Bool = false
    @Field public var extraPartnerParams: [String: String] = [:]
}

/// Type‑safe bridge for the JS `EnhancedDocumentVerificationParams` object
struct EnhancedDocumentVerificationParams: Record {
    @Field public var userId: String?
    @Field public var jobId: String?
    @Field public var countryCode: String
    @Field public var allowNewEnroll: Bool = false
    @Field public var documentType: String?
    @Field public var idAspectRatio: Double?
    @Field public var bypassSelfieCaptureWithFile: String?
    @Field public var autoCaptureTimeout: Int = 10
    @Field public var autoCapture: AutoCaptureParams = .autoCapture
    @Field public var captureBothSides: Bool = true
    @Field public var allowAgentMode: Bool = false
    @Field public var forceAgentMode: Bool = false
    @Field public var allowGalleryUpload: Bool = false
    @Field public var showInstructions: Bool = true
    @Field public var showAttribution: Bool = true
    @Field public var skipApiSubmission: Bool = false
    @Field public var useStrictMode: Bool = false
    @Field public var extraPartnerParams: [String: String] = [:]
    @Field public var consentInformation: ConsentInformationRecord?
}

/// Type‑safe bridge for the JS `ConsentInformationParams` object
struct ConsentInformationRecord: Record {
    @Field public var consentGrantedDate: String
    @Field public var personalDetails: Bool
    @Field public var contactInformation: Bool
    @Field public var documentInformation: Bool
}

/// Map `ConsentInformationRecord` to `ConsentInformation`
extension ConsentInformationRecord {
    func toConsentInformation() -> ConsentInformation {
        ConsentInformation(
            consented: ConsentedInformation(
                consentGrantedDate: consentGrantedDate,
                personalDetails: personalDetails,
                contactInformation: contactInformation,
                documentInformation: documentInformation
            )
        )
    }
}

/// Type‑safe bridge for the JS `SmartSelfieParams` object
struct SmartSelfieParams: Record {
    @Field public var userId: String?
    @Field public var jobId: String?
    @Field public var allowNewEnroll: Bool = false
    @Field public var allowAgentMode: Bool = false
    @Field public var forceAgentMode: Bool = false
    @Field public var showAttribution: Bool = true
    @Field public var showInstructions: Bool = true
    @Field public var skipApiSubmission: Bool = false
    @Field public var useStrictMode: Bool = false
    @Field public var smileSensitivity: SmileSensitivityParams = .normal
    @Field public var extraPartnerParams: [String: String] = [:]
}

/// Type‑safe bridge for the JS `BiometricKYCParams` object
struct BiometricKYCParams: Record {
    @Field public var userId: String?
    @Field public var jobId: String?
    @Field public var allowNewEnroll: Bool = false
    @Field public var allowAgentMode: Bool = false
    @Field public var forceAgentMode: Bool = false
    @Field public var showAttribution: Bool = true
    @Field public var showInstructions: Bool = true
    @Field public var smileSensitivity: SmileSensitivityParams = .normal
    @Field public var skipApiSubmission: Bool = false
    @Field public var useStrictMode: Bool = false
    @Field public var extraPartnerParams: [String: String] = [:]
    @Field public var consentInformation: ConsentInformationRecord?
    @Field public var idInfo: IdInfoParams
}

/// Type‑safe bridge for the JS `IdInfoParams` object
struct IdInfoParams: Record {
    @Field public var country: String
    @Field public var idType: String?
    @Field public var idNumber: String?
    @Field public var firstName: String?
    @Field public var middleName: String?
    @Field public var lastName: String?
    @Field public var dob: String?
    @Field public var bankCode: String?
    @Field public var entered: Bool = false
}

/// Map `IdInfoParams` to `IdInfo`
extension IdInfoParams {
    func toIdInfo() -> IdInfo {
        IdInfo(
            country: country,
            idType: idType,
            idNumber: idNumber,
            firstName: firstName,
            middleName: middleName,
            lastName: lastName,
            dob: dob,
            entered: entered
        )
    }
}

/// Enum for auto-capture parameter
enum AutoCaptureParams: String, Enumerable {
    case autoCapture = "AutoCapture"
    case autoCaptureOnly = "AutoCaptureOnly"
    case manualCaptureOnly = "ManualCaptureOnly"
}

/// Map `AutoCaptureParams` to `AutoCapture`
extension AutoCapture {
    init(from param: AutoCaptureParams) {
        switch param {
        case .autoCapture:
            self = .autoCapture
        case .autoCaptureOnly:
            self = .autoCaptureOnly
        case .manualCaptureOnly:
            self = .manualCaptureOnly
        }
    }
}

/// Enum for SmileSensitivity parameter
enum SmileSensitivityParams: String, Enumerable {
    case normal = "Normal"
    case relaxed = "Relaxed"
}

/// Map `SmileSensitivityParams` to `SmileSensitivity`
extension SmileSensitivity {
    init(from param: SmileSensitivityParams) {
        switch param {
        case .normal:
            self = .normal
        case .relaxed:
            self = .relaxed
        }
    }
}
