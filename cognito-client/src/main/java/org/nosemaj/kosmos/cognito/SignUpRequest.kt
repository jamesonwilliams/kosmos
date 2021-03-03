package org.nosemaj.kosmos.cognito

import org.json.JSONObject

data class SignUpRequest(
    val username: String,
    val password: String,
    val clientId: String,
    val secretHash: String,
    val userAttributes: List<Pair<String, String>>
) {
    fun asJson(): JSONObject = JSONObject()
        .put("Username", username)
        .put("Password", password)
        .put("ClientId", clientId)
        .put("SecretHash", secretHash)
}
