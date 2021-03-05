package org.nosemaj.kosmos.ci

import org.nosemaj.kosmos.aws.AwsClient

// Cognito Identity
class CiClient(region: String = "us-east-1") {
    private val client = AwsClient(
        serviceId = "AWSCognitoIdentityService",
        endpoint = "https://cognito-identity.$region.amazonaws.com"
    )

    /*
      curl \
          -X POST \
          -H 'X-Amz-Target: AWSCognitoIdentityService.GetId' \
          -H 'Content-Type: application/x-amz-json-1.1' \
          -d '{ "IdentityPoolId": "us-east-1:blahblah" }' \
          https://cognito-identity.us-east-1.amazonaws.com
     */
    fun getId(request: GetIdRequest): GetIdResponse {
        val json = client.post("GetId", request.asJson())
        return GetIdResponse.from(json)
    }

    fun getCredentialsForIdentity(
        request: GetCredentialsForIdentityRequest
    ): GetCredentialsForIdentityResponse {
        val json = client.post("GetCredentialsForIdentity", request.asJson())
        return GetCredentialsForIdentityResponse.from(json)
    }
}
