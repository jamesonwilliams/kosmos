package org.nosemaj.kosmos

import org.nosemaj.kosmos.Tokens.InvalidTokens
import org.nosemaj.kosmos.Tokens.ValidTokens
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.cip.InitiateAuthRequest
import org.nosemaj.kosmos.storage.TokenStorage

internal class GetTokens(
    private val tokenStorage: TokenStorage,
    private val cipClient: CipClient,
    private val clientId: String,
    private val clientSecret: String
) {
    internal fun execute(): Tokens {
        if (tokenStorage.isEmpty()) {
            return InvalidTokens
        } else if (tokenStorage.isExpired()) {
            refresh()
        }
        val accessToken = tokenStorage.accessToken()
        val idToken = tokenStorage.idToken()
        return ValidTokens(accessToken, idToken)
    }

    private fun refresh() {
        val refreshToken = tokenStorage.refreshToken()
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
        tokenStorage.clear()
        if (authenticationResult.refreshToken != null) {
            tokenStorage.refreshToken(authenticationResult.refreshToken)
        } else {
            tokenStorage.refreshToken(refreshToken)
        }
        tokenStorage.accessToken(authenticationResult.accessToken)
        tokenStorage.idToken(authenticationResult.idToken)
        tokenStorage.expiresIn(authenticationResult.expiresIn)
        tokenStorage.tokenType(authenticationResult.tokenType)
    }
}
