package org.nosemaj.kosmos

import org.nosemaj.kosmos.cognito.Cognito
import org.nosemaj.kosmos.cognito.ConfirmSignUpRequest
import org.nosemaj.kosmos.util.SecretHash

class ConfirmRegistration(
    private val cognito: Cognito,
    private val clientId: String,
    private val clientSecret: String,
    private val username: String,
    private val confirmationCode: String
) {
    fun execute() {
        cognito.confirmSignUp(
            ConfirmSignUpRequest(
                clientId = clientId,
                secretHash = SecretHash.of(username, clientId, clientSecret),
                username = username,
                confirmationCode = confirmationCode
            )
        )
    }
}
