package org.nosemaj.kosmos

import android.util.Base64
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.nosemaj.kosmos.cip.AuthenticationResult
import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.cip.InitiateAuthRequest
import org.nosemaj.kosmos.cip.InitiateAuthResponse
import org.nosemaj.kosmos.cip.RespondToAuthChallengeRequest
import org.nosemaj.kosmos.storage.CredentialStorage
import org.nosemaj.kosmos.util.AuthenticationHelper
import org.nosemaj.kosmos.util.SecretHash

class SignIn(
    private val cipClient: CipClient,
    private val credentialStorage: CredentialStorage,
    private val clientId: String,
    private val clientSecret: String,
    private val poolId: String,
    private val username: String,
    private val password: String
) {
    private val helper = AuthenticationHelper(poolId)

    fun execute() {
        @Suppress("UsePropertyAccessSyntax") // getA() is NOT "a"!!!!!!
        val response = cipClient.initiateAuth(
            InitiateAuthRequest(
                clientId = clientId,
                authFlow = "USER_SRP_AUTH",
                authParameters = mapOf(
                    "USERNAME" to username,
                    "SRP_A" to helper.getA().toString(16),
                    "SECRET_HASH" to SecretHash.of(username, clientId, clientSecret)
                )
            )
        )

        if (!response.hasChallengeParameters) {
            if (response.authenticationResult != null) {
                storeCredentials(response.authenticationResult!!)
            }
        } else when (response.challengeName) {
            "PASSWORD_VERIFIER" -> verifyPassword(password, response)
            else -> throw Exception("Challenge ${response.challengeName} not understood.")
        }
    }

    private fun verifyPassword(password: String, initAuthResponse: InitiateAuthResponse) {
        val challengeParameters = initAuthResponse.challengeParameters!!
        val salt = BigInteger(challengeParameters["SALT"]!!, 16)
        val secretBlock = challengeParameters["SECRET_BLOCK"]!!
        val userIdForSrp = challengeParameters["USER_ID_FOR_SRP"]!!
        val username = challengeParameters["USERNAME"]!!
        val srpB = BigInteger(challengeParameters["SRP_B"]!!, 16)
        val timestamp = computeTimestamp()

        val key = helper.getPasswordAuthenticationKey(userIdForSrp, password, srpB, salt)
        val claimSignature = claimSignature(userIdForSrp, key, timestamp, secretBlock)

        val request = RespondToAuthChallengeRequest(
            challengeName = initAuthResponse.challengeName!!,
            clientId = clientId,
            challengeResponses = mapOf(
                "SECRET_HASH" to SecretHash.of(username, clientId, clientSecret),
                "PASSWORD_CLAIM_SIGNATURE" to claimSignature,
                "PASSWORD_CLAIM_SECRET_BLOCK" to secretBlock,
                "TIMESTAMP" to timestamp,
                "USERNAME" to username
            ),
            session = initAuthResponse.session
        )
        val responseToAuthChallenge = cipClient.respondToAuthChallenge(request)
        val authResult = responseToAuthChallenge.authenticationResult
        storeCredentials(authResult)
    }

    // calculateSignature(hkdf, userPoolId, ChallengeParameters.USER_ID_FOR_SRP, ChallengeParameters.SECRET_BLOCK, dateNow)
    private fun claimSignature(
        userIdForSrp: String,
        key: ByteArray,
        timestamp: String,
        secretBlock: String
    ): String {
        val algorithm = "HmacSHA256"
        val mac = Mac.getInstance(algorithm)
        val keySpec = SecretKeySpec(key, algorithm)
        mac.init(keySpec)
        mac.update(poolId.split("_")[1].toByteArray())
        mac.update(userIdForSrp.toByteArray())
        mac.update(Base64.decode(secretBlock, Base64.NO_WRAP))

        val hmac = mac.doFinal(timestamp.toByteArray())
        return Base64.encodeToString(hmac, Base64.NO_WRAP)
    }

    private fun computeTimestamp(): String {
        val simpleDateFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return simpleDateFormat.format(Date())
    }

    private fun storeCredentials(authResult: AuthenticationResult) {
        credentialStorage.accessToken(authResult.accessToken)
        credentialStorage.idToken(authResult.idToken)
        credentialStorage.refreshToken(authResult.refreshToken)
        credentialStorage.expiresIn(authResult.expiresIn)
        credentialStorage.tokenType(authResult.tokenType)
    }
}
