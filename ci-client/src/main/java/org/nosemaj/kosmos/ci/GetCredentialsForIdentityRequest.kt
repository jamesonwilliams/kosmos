package org.nosemaj.kosmos.ci

import org.json.JSONObject

data class GetCredentialsForIdentityRequest(
    val customRoleArn: String? = null,
    val identityId: String,
    val logins: Map<String, String>
) {

    fun asJson(): JSONObject {
        return JSONObject()
            .put("CustomRoleArn", customRoleArn)
            .put("IdentityId", identityId)
            .put("Logins", JSONObject(logins))
    }
}
