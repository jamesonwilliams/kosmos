package org.nosemaj.kosmos.cognito

import org.json.JSONObject

data class RespondToAuthChallengeResponse(
    val authenticationResult: AuthenticationResult,
    val challengeName: String?
) {
    companion object {
        fun from(json: JSONObject): RespondToAuthChallengeResponse {
            val authResultJson = json.getJSONObject("AuthenticationResult")
            val challengeName = if (json.has("ChallengeName")) {
                json.getString("ChallengeName")
            } else null

            return RespondToAuthChallengeResponse(
                challengeName = challengeName,
                authenticationResult = AuthenticationResult(
                    accessToken = authResultJson.getString("AccessToken"),
                    expiresIn = authResultJson.getInt("ExpiresIn"),
                    idToken = authResultJson.getString("IdToken"),
                    refreshToken = authResultJson.getString("RefreshToken"),
                    tokenType = authResultJson.getString("TokenType")
                )
            )
        }
    }
}
