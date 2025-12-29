package ai.mysmartassistant.mysa.data.auth

import ai.mysmartassistant.mysa.R
import ai.mysmartassistant.mysa.ui.auth.TruecallerUiConfig
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import java.math.BigInteger
import java.security.SecureRandom

private const val TAG = "TruecallerOAuth"

class TruecallerOAuthManager(
    private val activity: FragmentActivity
) {

    private var isOAuthInProgress: Boolean = false
    private lateinit var state: String
    private lateinit var codeVerifier: String
    private var usable: Boolean = false

    private var listener:
            ((Result<TrueCallerAuthPayload>) -> Unit)? = null

    private val launcher =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d(TAG, "ActivityResult obtained: ${result.resultCode}")
            try {
                TcSdk.getInstance().onActivityResultObtained(
                    activity,
                    result.resultCode,
                    result.data
                )
            } catch (e: RuntimeException) {
                Log.e(TAG, "onActivityResultObtained exception", e)
                listener?.invoke(Result.failure(e))
                reset()
            }
        }

    private val callback = object : TcOAuthCallback {

        override fun onSuccess(tcOAuthData: TcOAuthData) {
            val receivedState = tcOAuthData.state
            Log.d(TAG, "onSuccess: state=$receivedState")

            if (receivedState != state) {
                Log.e(TAG, "State mismatch! Expected: $state, Received: $receivedState")
                listener?.invoke(
                    Result.failure(
                        IllegalStateException("OAuth state mismatch")
                    )
                )
                reset()
                return
            }

            val payload = TrueCallerAuthPayload(
                authorizationCode = tcOAuthData.authorizationCode,
                codeVerifier = codeVerifier,
                state = state,
                clientId = activity.getString(R.string.clientID)
            )
            Log.d(TAG, "Payload created, invoking success listener")

            listener?.invoke(Result.success(payload))
            reset()
        }

        override fun onFailure(error: TcOAuthError) {
            Log.e(TAG, "onFailure: ${error.errorCode} - ${error.errorMessage}")
            listener?.invoke(
                Result.failure(RuntimeException(error.errorMessage))
            )
            reset()
        }

        override fun onVerificationRequired(error: TcOAuthError?) {
            Log.e(TAG, "onVerificationRequired: ${error?.errorCode} - ${error?.errorMessage}")
            listener?.invoke(
                Result.failure(
                    RuntimeException(
                        error?.errorMessage
                            ?: "Truecaller flow cancelled"
                    )
                )
            )
            reset()
        }
    }

    fun init(uiConfig: TruecallerUiConfig) {
        Log.d(TAG, "init called")
        try {
            val options = TcSdkOptions.Builder(activity, callback)
                .buttonColor(uiConfig.buttonColor)
                .buttonTextColor(uiConfig.buttonTextColor)
                .buttonShapeOptions(
                    TcSdkOptions.BUTTON_SHAPE_ROUNDED
                )
                .loginTextPrefix(
                    TcSdkOptions.LOGIN_TEXT_PREFIX_TO_GET_STARTED
                )
                .build()

            TcSdk.init(options)
            usable = TcSdk.getInstance().isOAuthFlowUsable()
            Log.d(TAG, "init success, usable=$usable")
        } catch (e: RuntimeException) {
            Log.e(TAG, "init failed", e)
            usable = false
        }
    }

    fun isUsable(): Boolean {
        return try {
            val isUsable = usable && TcSdk.getInstance().isOAuthFlowUsable()
            Log.d(TAG, "isUsable check: $isUsable")
            isUsable
        } catch (e: RuntimeException) {
            Log.e(TAG, "isUsable exception", e)
            false
        }
    }

    fun startOAuth(
        listener: (Result<TrueCallerAuthPayload>) -> Unit
    ) {
        Log.d(TAG, "startOAuth called")
        if (isOAuthInProgress) {
            Log.d(TAG, "OAuth already in progress, ignoring")
            return // Ignore double taps
        }

        this.listener = listener
        isOAuthInProgress = true
        state = BigInteger(130, SecureRandom()).toString(32)
        codeVerifier =
            CodeVerifierUtil.generateRandomCodeVerifier()

        val codeChallenge =
            CodeVerifierUtil.getCodeChallenge(codeVerifier)
        if (codeChallenge == null) {
            Log.e(TAG, "Code challenge generation failed")
            listener.invoke(
                Result.failure(
                    IllegalStateException("Code challenge generation failed")
                )
            )
            return
        }
        TcSdk.getInstance().setOAuthState(state)
        TcSdk.getInstance().setCodeChallenge(codeChallenge)
        TcSdk.getInstance().setOAuthScopes(arrayOf("profile", "phone", "openid"))

        Log.d(TAG, "Launching Truecaller SDK activity")
        TcSdk.getInstance().getAuthorizationCode(
            activity,
            launcher
        )
    }

    private fun reset() {
        isOAuthInProgress = false
        listener = null
    }
}