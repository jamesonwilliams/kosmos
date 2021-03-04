package org.nosemaj.kosmos

import org.nosemaj.kosmos.cognito.Cognito
import org.nosemaj.kosmos.storage.CredentialStorage

class Auth(
    private val poolId: String,
    private val clientId: String,
    private val clientSecret: String,
    private val credentialStorage: CredentialStorage,
    region: String = "us-east-1",
    private val cognito: Cognito = Cognito(region)
) {
    fun signIn(username: String, password: String) {
        return SignIn(
            cognito,
            credentialStorage,
            clientId,
            clientSecret,
            poolId,
            username,
            password,
        ).execute()
    }

    fun session(): Session {
        return GetSession(credentialStorage, cognito, clientId, clientSecret)
            .execute()
    }

    fun registerUser(
        username: String,
        password: String,
        attributes: Map<String, String> = emptyMap()
    ): Registration {
        return RegisterUser(
            cognito,
            clientId,
            clientSecret,
            username,
            password,
            attributes
        ).execute()
    }

    fun confirmRegistration(username: String, confirmationCode: String) {
        return ConfirmRegistration(
            cognito,
            clientId,
            clientSecret,
            username,
            confirmationCode
        ).execute()
    }

    fun signOut() {
        return SignOut(cognito, credentialStorage).execute()
    }
}
