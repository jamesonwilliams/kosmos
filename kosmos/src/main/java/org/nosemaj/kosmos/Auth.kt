package org.nosemaj.kosmos

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.storage.CredentialStorage
import org.nosemaj.kosmos.storage.SecureCredentialStorage

class Auth(
    context: Context,
    private val poolId: String,
    private val clientId: String,
    private val clientSecret: String,
    private val credentialStorage: CredentialStorage = SecureCredentialStorage(context),
    private val cipClient: CipClient = CipClient(poolId.substringBefore('_')),
) {
    suspend fun signIn(username: String, password: String) = withContext(Dispatchers.IO) {
        return@withContext SignIn(
            cipClient,
            credentialStorage,
            clientId,
            clientSecret,
            poolId,
            username,
            password,
        ).execute()
    }

    suspend fun session(): Session = withContext(Dispatchers.IO) {
        return@withContext GetSession(
            credentialStorage, cipClient, clientId, clientSecret
        ).execute()
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
        return@withContext SignOut(cipClient, credentialStorage).execute()
    }
}
