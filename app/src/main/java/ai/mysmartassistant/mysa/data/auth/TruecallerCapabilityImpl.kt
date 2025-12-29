package ai.mysmartassistant.mysa.data.auth

import ai.mysmartassistant.mysa.domain.auth.TruecallerCapability
import com.truecaller.android.sdk.oAuth.TcSdk
import jakarta.inject.Inject

class TruecallerCapabilityImpl @Inject constructor() : TruecallerCapability {

    override fun isUsable(): Boolean {
        return try {
            TcSdk.getInstance().isOAuthFlowUsable()
        } catch (e: RuntimeException) {
            false
        }
    }
}