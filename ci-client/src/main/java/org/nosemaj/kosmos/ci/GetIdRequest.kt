package org.nosemaj.kosmos.ci

import org.json.JSONObject

data class GetIdRequest(
    val accountId: String,
    val identityPoolId: String,
    val logins: Map<String, String>
) {
    fun asJson(): JSONObject {
        return JSONObject()
            .put("AccountId", accountId)
            .put("IdentityPoolId", identityPoolId)
            .put("Logins", JSONObject(logins))
    }
}
