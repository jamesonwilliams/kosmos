package org.nosemaj.kosmos.cognito

import org.json.JSONObject

data class RespondToAuthChallengeRequest(
    val challengeName: String,
    val clientId: String,
    val challengeResponses: Map<String, String>?,
    val session: String?
) {
    fun asJson(): JSONObject {
        val json = JSONObject()
            .put("ChallengeName", challengeName)
            .put("ClientId", clientId)

        if (session != null) {
            json.put("Session", session)
        }
        if (challengeResponses != null) {
            json.put("ChallengeResponses", JSONObject(challengeResponses))
        }

        return json
    }
}
