import ExpoModulesCore
import SmileID
import SwiftUI

// Document Verification View using ExpoView
final class SmileIDDocumentVerificationView: ExpoView {
    let onResult = EventDispatcher()
    let onError = EventDispatcher()
    private let delegate: DocumentVerificationDelegate
    private let hostingController: UIHostingController<DocumentVerificationView>
    private var config: DocumentVerificationParams?

    required init(appContext: AppContext? = nil) {
        delegate = DocumentVerificationDelegate()
        hostingController = UIHostingController(
            rootView: DocumentVerificationView(
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

    func updateConfig(_ config: DocumentVerificationParams) {
        self.config = config
        hostingController.rootView = DocumentVerificationView(
            delegate: delegate,
            config: config
        )
    }
}

// SwiftUI view that wraps the SmileID document verification screen
struct DocumentVerificationView: View {
    let delegate: DocumentVerificationDelegate
    let config: DocumentVerificationParams?

    var body: some View {
        if let config = config {
            SmileID.documentVerificationScreen(
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
                showAttribution: config.showAttribution,
                smileSensitivity: SmileSensitivity(from: config.smileSensitivity),
                skipApiSubmission: config.skipApiSubmission,
                useStrictMode: config.useStrictMode,
                extraPartnerParams: config.extraPartnerParams,
                delegate: delegate
            )
        } else {
            Text("Configuration not provided").foregroundColor(.red)
        }
    }
}

// Delegate class for document verification
class DocumentVerificationDelegate: DocumentVerificationResultDelegate {
    var onResult: (([String: Any]) -> Void)?
    var onError: ((Error) -> Void)?

    func didSucceed(
        selfie: URL,
        documentFrontImage: URL,
        documentBackImage: URL?,
        didSubmitDocumentVerificationJob: Bool
    ) {
        var params: [String: Any] = [
            "selfieFile": selfie.absoluteString,
            "documentFrontFile": documentFrontImage.absoluteString,
            "didSubmitDocumentVerificationJob": didSubmitDocumentVerificationJob,
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
