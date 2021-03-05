package org.nosemaj.kosmos

import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.cip.ConfirmSignUpRequest
import org.nosemaj.kosmos.util.SecretHash

class ConfirmRegistration(
    private val cipClient: CipClient,
    private val clientId: String,
    private val clientSecret: String,
    private val username: String,
    private val confirmationCode: String
) {
    fun execute() {
        cipClient.confirmSignUp(
            ConfirmSignUpRequest(
                clientId = clientId,
                secretHash = SecretHash.of(username, clientId, clientSecret),
                username = username,
                confirmationCode = confirmationCode
            )
        )
    }
}
