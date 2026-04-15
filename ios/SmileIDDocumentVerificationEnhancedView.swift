import ExpoModulesCore
import SmileID
import SwiftUI

// Enhanced Document Verification View using ExpoView
final class SmileIDDocumentVerificationEnhancedView: ExpoView {
    let onResult = EventDispatcher()
    let onError = EventDispatcher()
    private let delegate: EnhancedDocumentVerificationDelegate
    private let hostingController: UIHostingController<EnhancedDocumentVerificationView>
    private var config: EnhancedDocumentVerificationParams?

    required init(appContext: AppContext? = nil) {
        delegate = EnhancedDocumentVerificationDelegate()
        hostingController = UIHostingController(
            rootView: EnhancedDocumentVerificationView(
                delegate: delegate,
                config: nil
            )
        )
        // Force light mode
        hostingController.overrideUserInterfaceStyle = .light

        super.init(appContext: appContext)

        // Set up delegate callbacks
        delegate.onResult = { [weak self] result in
            self?.onResult(result)
        }

        delegate.onError = { [weak self] error in
            self?.onError(["error": error.localizedDescription])
        }

        // Add the hosting controller's view
        addSubview(hostingController.view)
        hostingController.view.fillSuperview()
    }

    func updateConfig(_ config: EnhancedDocumentVerificationParams) {
        self.config = config
        hostingController.rootView = EnhancedDocumentVerificationView(
            delegate: delegate,
            config: config
        )
    }
}

// SwiftUI view that wraps the SmileID enhanced document verification screen
struct EnhancedDocumentVerificationView: View {
    let delegate: EnhancedDocumentVerificationDelegate
    let config: EnhancedDocumentVerificationParams?

    var body: some View {
        if let config = config {
            SmileID.enhancedDocumentVerificationScreen(
                userId: config.userId ?? generateUserId(),
                jobId: config.jobId ?? generateJobId(),
                allowNewEnroll: config.allowNewEnroll,
                countryCode: config.countryCode,
                documentType: config.documentType,
                idAspectRatio: config.idAspectRatio,
                bypassSelfieCaptureWithFile: config.bypassSelfieCaptureWithFile.flatMap(URL.init),
                autoCaptureTimeout: TimeInterval(config.autoCaptureTimeout),
                autoCapture: AutoCapture(from: config.autoCapture),
                captureBothSides: config.captureBothSides,
                allowAgentMode: config.allowAgentMode,
                forceAgentMode: config.forceAgentMode,
                allowGalleryUpload: config.allowGalleryUpload,
                showInstructions: config.showInstructions,
                skipApiSubmission: config.skipApiSubmission,
                showAttribution: config.showAttribution,
                useStrictMode: config.useStrictMode,
                extraPartnerParams: config.extraPartnerParams,
                consentInformation: config.consentInformation?.toConsentInformation(),
                delegate: delegate
            )
        } else {
            Text("Configuration not provided").foregroundColor(.red)
        }
    }
}


// Delegate class for enhanced document verification
class EnhancedDocumentVerificationDelegate: EnhancedDocumentVerificationResultDelegate {
    var onResult: (([String: Any]) -> Void)?
    var onError: ((Error) -> Void)?

    func didSucceed(
        selfie: URL,
        documentFrontImage: URL,
        documentBackImage: URL?,
        didSubmitEnhancedDocVJob: Bool
    ) {
        var params: [String: Any] = [
            "selfieFile": selfie.absoluteString,
            "documentFrontFile": documentFrontImage.absoluteString,
            "didSubmitEnhancedDocVJob": didSubmitEnhancedDocVJob,
        ]
        if let documentBackImage {
            params["documentBackFile"] = documentBackImage.absoluteString
        }
        onResult?(params)
    }

    func didError(error: Error) {
        onError?(error)
    }
}
