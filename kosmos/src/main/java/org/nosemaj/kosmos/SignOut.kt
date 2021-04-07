package org.nosemaj.kosmos

import android.util.Base64
import java.lang.IllegalStateException
import org.json.JSONObject
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.cip.IntrospectTokenRequest
import org.nosemaj.kosmos.cip.RevokeTokenRequest
import org.nosemaj.kosmos.storage.TokenStorage

class SignOut(
    private val client: CipClient,
    private val tokenStorage: TokenStorage,
    private val clientId: String,
    private val clientSecret: String
) {
    fun execute() {
        if (!isSessionRevocable(tokenStorage.idToken())) {
            tokenStorage.clear()
            return
        }

        revokeTokens()
        tokenStorage.clear()
    }

    private fun revokeTokens() {
        listOf(
            tokenStorage.accessToken(),
            tokenStorage.idToken(),
            tokenStorage.refreshToken()
        )
            .forEach { token ->
                client.revokeToken(
                    RevokeTokenRequest(
                        token = token,
                        clientId = clientId,
                        clientSecret = clientSecret
                    )
                )
                validateRevocation(token)
            }
    }

    private fun validateRevocation(token: String) {
        val request = IntrospectTokenRequest(
            token = token,
            clientId = clientId,
            clientSecret = clientSecret
        )
        if (client.introspectToken(request).active) {
            throw IllegalStateException("Token is active after revocation! $token")
        }
    }

    private fun isSessionRevocable(idToken: String): Boolean {
        val base64Claim = idToken.split('.')[1]
        val claimsJson = JSONObject(String(Base64.decode(base64Claim, Base64.NO_WRAP)))
        return claimsJson.has("jti")
    }
}
