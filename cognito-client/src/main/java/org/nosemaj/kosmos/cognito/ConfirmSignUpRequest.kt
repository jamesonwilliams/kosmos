package org.nosemaj.kosmos.cognito

import org.json.JSONObject

data class ConfirmSignUpRequest(
    val secretHash: String,
    val clientId: String,
    val username: String,
    val confirmationCode: String
) {
    fun asJson(): JSONObject = JSONObject()
        .put("SecretHash", secretHash)
        .put("ClientId", clientId)
        .put("Username", username)
        .put("ConfirmationCode", confirmationCode)
}
