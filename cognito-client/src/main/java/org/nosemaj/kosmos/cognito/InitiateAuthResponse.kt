package org.nosemaj.kosmos.cognito

import org.json.JSONObject

data class InitiateAuthResponse(
    val challengeParameters: Map<String, String>?,
    val authenticationResult: AuthenticationResult?,
    val hasChallengeParameters: Boolean,
    val challengeName: String?,
    val session: String?
) {
    companion object {
        fun from(json: JSONObject): InitiateAuthResponse {
            val challengeParamJson = json.optJSONObject("ChallengeParameters")
            val challengeParameters: Map<String, String>? = if (challengeParamJson != null) {
                val map = mutableMapOf<String, String>()
                for (key in challengeParamJson.keys()) {
                    map[key] = challengeParamJson[key] as String
                }
                map
            } else {
                null
            }

            val authenticationResult = if (json.has("AuthenticationResult")) {
                val authResultJson = json.getJSONObject("AuthenticationResult")
                AuthenticationResult(
                    accessToken = authResultJson.getString("AccessToken"),
                    expiresIn = authResultJson.getInt("ExpiresIn"),
                    idToken = authResultJson.getString("IdToken"),
                    refreshToken = authResultJson.getString("RefreshToken"),
                    tokenType = authResultJson.getString("TokenType")
                )
            } else {
                null
            }

            val session = if (json.has("Session")) {
                json.getString("Session")
            } else {
                null
            }

            return InitiateAuthResponse(
                challengeName = json.getString("ChallengeName"),
                challengeParameters = challengeParameters,
                session = session,
                authenticationResult = authenticationResult,
                hasChallengeParameters = challengeParameters != null
            )
        }
    }
}
