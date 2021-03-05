package org.nosemaj.kosmos.cip

import org.json.JSONObject

data class RespondToAuthChallengeResponse(
    val authenticationResult: AuthenticationResult,
    val challengeName: String?
) {
    companion object {
        fun from(json: JSONObject): RespondToAuthChallengeResponse {
            val authResultJson = json.getJSONObject("AuthenticationResult")
            return RespondToAuthChallengeResponse(
                challengeName = json.maybeString("ChallengeName"),
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
