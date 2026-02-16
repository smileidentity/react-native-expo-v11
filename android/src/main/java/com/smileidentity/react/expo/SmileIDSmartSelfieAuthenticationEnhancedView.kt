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
import com.smileidentity.compose.SmartSelfieAuthenticationEnhanced
import com.smileidentity.results.SmartSelfieResult
import com.smileidentity.results.SmileIDResult
import com.smileidentity.util.randomUserId
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher

/**
 * Enhanced Smart Selfie Authentication View using ExpoView
 **/
class SmileIDSmartSelfieAuthenticationEnhancedView(context: Context, appContext: AppContext) :
    SmileIDExpoComposeView(
        context = context,
        appContext = appContext,
        shouldHostComposeContent = true
    ) {
    private var props = mutableStateOf(SmartSelfieProps())
    private val onResult by EventDispatcher()
    private val onError by EventDispatcher()

    @Composable
    override fun Content() {
        EnhancedSmartSelfieAuthenticationView(
            props = props.value,
            onResult = { result ->
                onResult(
                    mapOf(
                        "selfieFile" to result.selfieFile.toString(),
                        "livenessFiles" to result.livenessFiles.toString()
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

    fun updateConfig(config: SmartSelfieParams) {
        props.value = config.toSmartSelfieProps()
    }
}

/**
 * Compose view that wraps the SmileID Enhanced SmartSelfie enrollment screen
 **/
@Composable
private fun EnhancedSmartSelfieAuthenticationView(
    props: SmartSelfieProps,
    onResult: (SmartSelfieResult) -> Unit,
    onError: (Throwable) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()
    ) {
        SmileID.SmartSelfieAuthenticationEnhanced(
            userId = props.userId ?: randomUserId(),
            allowNewEnroll = props.allowNewEnroll,
            showAttribution = props.showAttribution,
            showInstructions = props.showInstructions,
            skipApiSubmission = props.skipApiSubmission,
            extraPartnerParams = props.extraPartnerParams,
        ) { result ->
            when (result) {
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