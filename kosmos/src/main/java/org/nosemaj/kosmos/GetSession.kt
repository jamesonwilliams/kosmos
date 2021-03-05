package org.nosemaj.kosmos

import org.nosemaj.kosmos.Session.AuthenticatedSession
import org.nosemaj.kosmos.Session.Credentials
import org.nosemaj.kosmos.ci.CiClient
import org.nosemaj.kosmos.ci.GetCredentialsForIdentityRequest
import org.nosemaj.kosmos.ci.GetIdRequest

class GetSession(
    private val ciClient: CiClient,
    private val identityPoolId: String,
    private val userPoolId: String,
    private val idToken: String
) {
    fun execute(): Session {
        val region = identityPoolId.substringBefore(":")
        val logins = mapOf("cognito-idp.$region.amazonaws.com/$userPoolId" to idToken)
        val idResponse = ciClient.getId(
            GetIdRequest(
                identityPoolId = identityPoolId,
                logins = logins
            )
        )
        val credentialsRequest = GetCredentialsForIdentityRequest(
            identityId = idResponse.identityId,
            logins = logins
        )
        val credentialsResponse = ciClient.getCredentialsForIdentity(credentialsRequest)
        val credentials = credentialsResponse.credentials
        return AuthenticatedSession(
            identityId = credentialsResponse.identityId,
            credentials = Credentials(
                accessKeyId = credentials.accessKeyId,
                secretKey = credentials.secretKey,
                sessionToken = credentials.sessionToken
            )
        )
    }
}
