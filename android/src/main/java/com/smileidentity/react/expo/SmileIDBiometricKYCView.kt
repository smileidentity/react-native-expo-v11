package com.smileidentity.react.expo

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.smileidentity.SmileID
import com.smileidentity.compose.BiometricKYC
import com.smileidentity.models.ConsentInformation
import com.smileidentity.models.IdInfo
import com.smileidentity.models.SmileSensitivity
import com.smileidentity.results.BiometricKycResult
import com.smileidentity.results.SmileIDResult
import com.smileidentity.util.randomJobId
import com.smileidentity.util.randomUserId
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

/**
 * Biometric KYC View using ExpoView
 **/
class SmileIDBiometricKYCView(context: Context, appContext: AppContext) : SmileIDExpoComposeView(
    context = context,
    appContext = appContext,
    shouldHostComposeContent = true
) {
    private var props = mutableStateOf(BiometricKYCProps())
    private val onResult by EventDispatcher()
    private val onError by EventDispatcher()

    @Composable
    override fun Content() {
        BiometricKYCView(
            props = props.value,
            onResult = { result ->
                onResult(
                    mapOf(
                        "selfieFile" to result.selfieFile.toString(),
                        "livenessFiles" to result.livenessFiles.toString(),
                        "didSubmitBiometricJob" to result.didSubmitBiometricKycJob
                    )
                )
            },
            onError = { error ->
                onError(
                    mapOf(
                        "error" to error.localizedMessage.toString()
                    )
                )
            }
        )
    }

    fun updateConfig(config: BiometricKYCParams) {
        props.value = config.toBiometricKYCProps()
    }
}

/**
 * Compose view that wraps the SmileID Biometric KYV screen
 **/
@Composable
private fun BiometricKYCView(
    props: BiometricKYCProps,
    onResult: (BiometricKycResult) -> Unit,
    onError: (Throwable) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()
    ) {
        SmileID.BiometricKYC(
            userId = props.userId ?: randomUserId(),
            jobId = props.jobId ?: randomJobId(),
            allowNewEnroll = props.allowNewEnroll,
            allowAgentMode = props.allowAgentMode,
            showAttribution = props.showAttribution,
            showInstructions = props.showInstructions,
            smileSensitivity = props.smileSensitivity,
            extraPartnerParams = props.extraParams,
            consentInformation = props.consentInformation,
            idInfo = props.idInfo
        ) { result ->
            when(result) {
                is SmileIDResult.Success -> {
                    onResult(result.data)
                }
                is SmileIDResult.Error -> {
                    onError(result.throwable)
                }
            }
        }
    }
}

data class BiometricKYCProps(
    val userId: String? = null,
    val jobId: String? = null,
    val allowNewEnroll: Boolean = true,
    val allowAgentMode: Boolean = false,
    val showAttribution: Boolean = true,
    val showInstructions: Boolean = true,
    val skipApiSubmission: Boolean = false,
    val smileSensitivity: SmileSensitivity = SmileSensitivity.NORMAL,
    val useStrictMode: Boolean = false,
    val extraParams: ImmutableMap<String, String> = persistentMapOf(),
    val consentInformation: ConsentInformation ? = null,
    val idInfo: IdInfo = IdInfo(
        country = ""
    )
)
