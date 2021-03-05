package org.nosemaj.kosmos.ci

import org.json.JSONObject

data class GetCredentialsForIdentityResponse(
    val identityId: String,
    val credentials: Credentials
) {
    data class Credentials(
        val accessKeyId: String,
        val expiration: Int,
        val secretKey: String,
        val sessionToken: String
    )

    companion object {
        fun from(json: JSONObject): GetCredentialsForIdentityResponse {
            val credentials = json.getJSONObject("Credentials")
            return GetCredentialsForIdentityResponse(
                json.getString("IdentityId"),
                Credentials(
                    credentials.getString("AccessKeyId"),
                    credentials.getInt("Expiration"),
                    credentials.getString("SecretKey"),
                    credentials.getString("SessionToken")
                )
            )
        }
    }
}
