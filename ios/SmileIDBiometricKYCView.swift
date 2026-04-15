import ExpoModulesCore
import SmileID
import SwiftUI

// Biometric KYC View using ExpoView
final class SmileIDBiometricKYCView: ExpoView {
    let onResult = EventDispatcher()
    let onError = EventDispatcher()
    private let delegate: BiometricKYCDelegate
    private let hostingController: UIHostingController<BiometricKYCView>
    private var config: BiometricKYCParams?

    required init(appContext: AppContext? = nil) {
        delegate = BiometricKYCDelegate()
        hostingController = UIHostingController(
            rootView: BiometricKYCView(
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

    func updateConfig(_ config: BiometricKYCParams) {
        self.config = config
        hostingController.rootView = BiometricKYCView(
            delegate: delegate,
            config: config
        )
    }
}

// SwiftUI view that wraps the SmileID biometric kyc screen
struct BiometricKYCView: View {
    let delegate: BiometricKYCDelegate
    let config: BiometricKYCParams?
    
    var body: some View {
        if let config = config {
            SmileID.biometricKycScreen(
                idInfo: config.idInfo.toIdInfo(),
                userId: config.userId ?? generateUserId(),
                jobId: config.jobId ?? generateJobId(),
                allowNewEnroll: config.allowNewEnroll,
                allowAgentMode: config.allowAgentMode,
                forceAgentMode: config.forceAgentMode,
                showAttribution: config.showAttribution,
                showInstructions: config.showInstructions,
                smileSensitivity: SmileSensitivity(from: config.smileSensitivity),
                useStrictMode: config.useStrictMode,
                skipApiSubmission: config.skipApiSubmission,
                extraPartnerParams: config.extraPartnerParams,
                consentInformation: config.consentInformation?.toConsentInformation(),
                delegate: delegate
            )
        } else {
            Text("Configuration not provided").foregroundColor(.red)
        }
    }
}

// Delegate class for biometric KYC
class BiometricKYCDelegate: BiometricKycResultDelegate {
    var onResult: (([String: Any]) -> Void)?
    var onError: ((Error) -> Void)?

    func didSucceed(
        selfieImage: URL,
        livenessImages: [URL],
        didSubmitBiometricJob: Bool
    ) {
        let result: [String: Any] = [
            "selfieImage": selfieImage.absoluteString,
            "livenessImages": livenessImages.map { $0.absoluteString },
            "didSubmitBiometricJob": didSubmitBiometricJob
        ]
        onResult?(result)
    }

    func didError(error: Error) {
        onError?(error)
    }
}
