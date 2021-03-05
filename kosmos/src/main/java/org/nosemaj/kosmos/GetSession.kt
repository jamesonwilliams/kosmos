package org.nosemaj.kosmos

import org.nosemaj.kosmos.Session.InvalidSession
import org.nosemaj.kosmos.Session.ValidSession
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.cip.InitiateAuthRequest
import org.nosemaj.kosmos.storage.CredentialStorage

internal class GetSession(
    private val credentialStorage: CredentialStorage,
    private val cipClient: CipClient,
    private val clientId: String,
    private val clientSecret: String
) {
    internal fun execute(): Session {
        if (credentialStorage.isEmpty()) {
            return InvalidSession()
        } else if (credentialStorage.isExpired()) {
            refresh()
        }
        val accessToken = credentialStorage.accessToken()
        val idToken = credentialStorage.idToken()
        return ValidSession(accessToken, idToken)
    }

    private fun refresh() {
        val refreshToken = credentialStorage.refreshToken()
        val parameters = mapOf(
            "REFRESH_TOKEN" to refreshToken,
            "SECRET_HASH" to clientSecret // Surprising, huh? I was surprised, too, Cognito.
        )
        val request = InitiateAuthRequest(
            authFlow = "REFRESH_TOKEN_AUTH",
            clientId = clientId,
            authParameters = parameters
        )
        val response = cipClient.initiateAuth(request)
        val authenticationResult = response.authenticationResult!!
        credentialStorage.clear()
        if (authenticationResult.refreshToken != null) {
            credentialStorage.refreshToken(authenticationResult.refreshToken)
        } else {
            credentialStorage.refreshToken(refreshToken)
        }
        credentialStorage.accessToken(authenticationResult.accessToken)
        credentialStorage.idToken(authenticationResult.idToken)
        credentialStorage.expiresIn(authenticationResult.expiresIn)
        credentialStorage.tokenType(authenticationResult.tokenType)
    }
}
