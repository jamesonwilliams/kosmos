package org.nosemaj.kosmos.cip

import org.json.JSONObject
import org.nosemaj.kosmos.aws.AwsClient

// CIP = Cognito Identity Provider
class CipClient(region: String = "us-east-1") {
    private val client = AwsClient(
        serviceId = "AWSCognitoIdentityProviderService",
        endpoint = "https://cognito-idp.$region.amazonaws.com"
    )

    fun confirmSignUp(request: ConfirmSignUpRequest) {
        client.post("ConfirmSignUp", request.asJson())
    }

    fun signUp(request: SignUpRequest): SignUpResponse {
        val json = client.post("SignUp", request.asJson())
        return SignUpResponse.from(json)
    }

    fun initiateAuth(request: InitiateAuthRequest): InitiateAuthResponse {
        val json = client.post("InitiateAuth", request.asJson())
        return InitiateAuthResponse.from(json)
    }

    fun revokeToken(request: RevokeTokenRequest) {
        client.post("RevokeToken", request.asJson())
    }

    fun introspectToken(request: IntrospectTokenRequest): IntrospectTokenResponse {
        val json = client.post("IntrospectToken", request.asJson())
        return IntrospectTokenResponse.from(json)
    }

    fun globalSignOut(accessToken: String) {
        val requestBody = JSONObject().put("AccessToken", accessToken)
        client.post("GlobalSignOut", requestBody)
    }

    fun respondToAuthChallenge(
        request: RespondToAuthChallengeRequest
    ): RespondToAuthChallengeResponse {
        val json = client.post("RespondToAuthChallenge", request.asJson())
        return RespondToAuthChallengeResponse.from(json)
    }
}
