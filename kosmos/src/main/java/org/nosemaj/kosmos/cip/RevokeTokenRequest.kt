package org.nosemaj.kosmos.cip

import org.json.JSONObject

data class RevokeTokenRequest(val token: String, val clientId: String, val clientSecret: String) {
    fun asJson(): JSONObject {
        return JSONObject()
            .put("Token", token)
            .put("ClientId", clientId)
            .put("ClientSecret", clientSecret)
    }
}
