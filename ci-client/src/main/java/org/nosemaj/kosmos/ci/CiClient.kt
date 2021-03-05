package org.nosemaj.kosmos.ci

import org.nosemaj.kosmos.aws.AwsClient

// Cognito Identity
class CiClient(region: String = "us-east-1") {
    private val client = AwsClient(
        serviceId = "AWSCognitoIdentityService",
        endpoint = "https://cognito-identity.$region.amazonaws.com"
    )

    fun getId(request: GetIdRequest): GetIdResponse {
        val json = client.post("GetId", request.asJson())
        return GetIdResponse.from(json)
    }

    fun getCredentialsForIdentity(request: GetCredentialsForIdentityRequest): GetCredentialsForIdentityResponse {
            val json = client.post("GetCredentialsForIdentity", request.asJson())
            return GetCredentialsForIdentityResponse.from(json)
        }
}
