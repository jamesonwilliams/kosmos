package org.nosemaj.kosmos.util

import android.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class SecretHash {
    companion object {
        fun of(username: String, clientId: String, clientSecret: String): String {
            val mac = Mac.getInstance("HmacSHA256")
            val secretBytes = clientSecret.toByteArray()
            mac.init(SecretKeySpec(secretBytes, "HmacSHA256"))
            val dataBytes = (username + clientId).toByteArray()
            val rawHmac = mac.doFinal(dataBytes)
            return Base64.encodeToString(rawHmac, Base64.NO_WRAP)
        }
    }
}
