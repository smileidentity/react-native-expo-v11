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
import com.smileidentity.compose.SmartSelfieEnrollment
import com.smileidentity.models.SmileSensitivity
import com.smileidentity.results.SmartSelfieResult
import com.smileidentity.results.SmileIDResult
import com.smileidentity.util.randomJobId
import com.smileidentity.util.randomUserId
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

/**
 * Smart Selfie Enrolment View using ExpoView
 **/
class SmileIDSmartSelfieEnrollmentView(context: Context, appContext: AppContext) :
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
        SmartSelfieEnrollmentView(
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
 * Compose view that wraps the SmileID SmartSelfie enrollment screen
 **/
@Composable
private fun SmartSelfieEnrollmentView(
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
        SmileID.SmartSelfieEnrollment(
            userId = props.userId ?: randomUserId(),
            jobId = props.jobId ?: randomJobId(),
            allowNewEnroll = props.allowNewEnroll,
            allowAgentMode = props.allowAgentMode,
            showAttribution = props.showAttribution,
            showInstructions = props.showInstructions,
            skipApiSubmission = props.skipApiSubmission,
            smileSensitivity = props.smileSensitivity,
            extraPartnerParams = props.extraPartnerParams,
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

data class SmartSelfieProps(
    val userId: String? = null,
    val jobId: String? = null,
    val allowNewEnroll: Boolean = true,
    val allowAgentMode: Boolean = false,
    val showAttribution: Boolean = true,
    val showInstructions: Boolean = true,
    val skipApiSubmission: Boolean = false,
    val useStrictMode: Boolean = false,
    val smileSensitivity: SmileSensitivity  = SmileSensitivity.NORMAL,
    val extraPartnerParams: ImmutableMap<String, String> = persistentMapOf()
)
