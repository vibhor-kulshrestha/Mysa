package ai.mysmartassistant.mysa.platform.sms

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever

object SmsRetrieverStarter {

    fun start(context: Context) {
        val client = SmsRetriever.getClient(context)
        client.startSmsRetriever()
    }
}