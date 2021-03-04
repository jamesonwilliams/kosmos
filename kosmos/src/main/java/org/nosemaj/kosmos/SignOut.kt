package org.nosemaj.kosmos

import org.nosemaj.kosmos.cognito.Cognito
import org.nosemaj.kosmos.storage.CredentialStorage

class SignOut(
    private val client: Cognito,
    private val credentialStorage: CredentialStorage
) {
    fun execute() {
        val accessToken = credentialStorage.accessToken()
        credentialStorage.clear()
        client.globalSignOut(accessToken = accessToken)
    }
}
