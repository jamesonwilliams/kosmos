package org.nosemaj.kosmos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.nosemaj.kosmos.Session.GuestSession
import org.nosemaj.kosmos.Tokens.InvalidTokens
import org.nosemaj.kosmos.Tokens.ValidTokens
import org.nosemaj.kosmos.ci.CiClient
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.storage.SecureTokenStorage
import org.nosemaj.kosmos.storage.TokenStorage

class Auth(
    context: Context,
    private val identityPoolId: String,
    private val userPoolId: String,
    private val clientId: String,
    private val clientSecret: String,
    private val tokenStorage: TokenStorage = SecureTokenStorage(context),
    private val cipClient: CipClient = CipClient(userPoolId.substringBefore('_')),
    private val ciClient: CiClient = CiClient(identityPoolId.substringBefore(":"))
) {
    suspend fun signIn(username: String, password: String) = withContext(Dispatchers.IO) {
        return@withContext SignIn(
            cipClient,
            tokenStorage,
            clientId,
            clientSecret,
            userPoolId,
            username,
            password,
        ).execute()
    }

    suspend fun tokens(): Tokens = withContext(Dispatchers.IO) {
        return@withContext GetTokens(
            tokenStorage, cipClient, clientId, clientSecret
        ).execute()
    }

    suspend fun session(): Session = withContext(Dispatchers.IO) {
        when (val tokens = tokens()) {
            is InvalidTokens -> GuestSession()
            is ValidTokens -> return@withContext GetSession(
                ciClient = ciClient,
                identityPoolId = identityPoolId,
                userPoolId = userPoolId,
                idToken = (tokens() as ValidTokens).idToken
            ).execute()
        }
    }

    suspend fun registerUser(
        username: String,
        password: String,
        attributes: Map<String, String> = emptyMap()
    ): Registration = withContext(Dispatchers.IO) {
        return@withContext RegisterUser(
            cipClient, clientId, clientSecret, username, password,
            attributes
        ).execute()
    }

    suspend fun confirmRegistration(
        username: String,
        confirmationCode: String
    ) = withContext(Dispatchers.IO) {
        return@withContext ConfirmRegistration(
            cipClient, clientId, clientSecret, username,
            confirmationCode
        ).execute()
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        return@withContext SignOut(cipClient, tokenStorage).execute()
    }
}
