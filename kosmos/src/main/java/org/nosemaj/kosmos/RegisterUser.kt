package org.nosemaj.kosmos

import org.nosemaj.kosmos.Registration.ConfirmedRegistration
import org.nosemaj.kosmos.Registration.UnconfirmedRegistration
import org.nosemaj.kosmos.cognito.Cognito
import org.nosemaj.kosmos.cognito.SignUpRequest
import org.nosemaj.kosmos.util.SecretHash.Companion

class RegisterUser(
    private val cognito: Cognito,
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
            userAttributes = attributes.map { Pair(it.key, it.value) }
        )
        val response = cognito.signUp(request)
        return if (!response.userConfirmed) {
            val details = response.codeDeliveryDetails
            UnconfirmedRegistration(
                deliveryMedium = details.deliveryMedium,
                attributeName = details.attributeName,
                destination = details.destination
            )
        } else {
            ConfirmedRegistration(
                username = username,
                userId = response.userSub,
            )
        }
    }
}
