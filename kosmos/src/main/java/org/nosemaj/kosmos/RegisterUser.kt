package org.nosemaj.kosmos

import org.nosemaj.kosmos.Registration.ConfirmedRegistration
import org.nosemaj.kosmos.Registration.UnconfirmedRegistration
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.cip.SignUpRequest
import org.nosemaj.kosmos.util.SecretHash.Companion

class RegisterUser(
    private val cipClient: CipClient,
    private val clientId: String,
    private val clientSecret: String,
    private val username: String,
    private val password: String,
    private val attributes: Map<String, String>
) {
    fun execute(): Registration {
        val request = SignUpRequest(
            username = username,
            password = password,
            clientId = clientId,
            secretHash = Companion.of(username, clientId, clientSecret),
            userAttributes = attributes
        )
        val response = cipClient.signUp(request)
        return if (!response.userConfirmed) {
            UnconfirmedRegistration
        } else {
            ConfirmedRegistration
        }
    }
}
