package org.nosemaj.kosmos.cip

import org.json.JSONArray
import org.json.JSONObject

data class SignUpRequest(
    val username: String,
    val password: String,
    val clientId: String,
    val secretHash: String,
    val userAttributes: Map<String, String>
) {
    fun asJson(): JSONObject {
        val attributes = JSONArray()
        for (attribute in userAttributes.entries) {
            attributes.put(
                JSONObject()
                    .put("Name", attribute.key)
                    .put("Value", attribute.value)
            )
        }

        return JSONObject()
            .put("Username", username)
            .put("Password", password)
            .put("UserAttributes", attributes)
            .put("ClientId", clientId)
            .put("SecretHash", secretHash)
    }
}
