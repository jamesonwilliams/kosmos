package org.nosemaj.kosmos.ci

import org.json.JSONObject

data class GetIdRequest(
    val accountId: String? = null,
    val identityPoolId: String,
    val logins: Map<String, String> = emptyMap()
) {
    fun asJson(): JSONObject {
        return JSONObject()
            .put("AccountId", accountId)
            .put("IdentityPoolId", identityPoolId)
            .put("Logins", JSONObject(logins))
    }
}
