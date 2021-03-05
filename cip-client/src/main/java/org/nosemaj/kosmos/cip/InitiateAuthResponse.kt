package org.nosemaj.kosmos.cip

import org.json.JSONObject
import org.nosemaj.kosmos.aws.maybeString

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
            val challengeParameters: Map<String, String>? = when (challengeParamJson) {
                null -> null
                else -> {
                    val map = mutableMapOf<String, String>()
                    for (key in challengeParamJson.keys()) {
                        map[key] = challengeParamJson[key] as String
                    }
                    map
                }
            }

            val authenticationResult = when {
                json.has("AuthenticationResult") -> {
                    val authResultJson = json.getJSONObject("AuthenticationResult")
                    AuthenticationResult(
                        accessToken = authResultJson.getString("AccessToken"),
                        expiresIn = authResultJson.getInt("ExpiresIn"),
                        idToken = authResultJson.getString("IdToken"),
                        refreshToken = authResultJson.maybeString("RefreshToken"),
                        tokenType = authResultJson.getString("TokenType")
                    )
                }
                else -> null
            }

            return InitiateAuthResponse(
                challengeName = json.maybeString("ChallengeName"),
                challengeParameters = challengeParameters,
                session = json.maybeString("Session"),
                authenticationResult = authenticationResult,
                hasChallengeParameters = challengeParameters != null
            )
        }
    }
}
