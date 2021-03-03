package org.nosemaj.kosmos.cognito

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import org.json.JSONObject
import org.nosemaj.kosmos.cognito.SignUpResponse.Companion

class Cognito(
    private val endpoint: String = "https://cognito-idp.us-east-1.amazonaws.com"
) {
    fun confirmSignUp(request: ConfirmSignUpRequest) {
        post("ConfirmSignUp", request.asJson())
    }

    fun signUp(request: SignUpRequest): SignUpResponse {
        val json = post("SignUp", request.asJson())
        return Companion.from(json)
    }

    fun initiateAuth(request: InitiateAuthRequest): InitiateAuthResponse {
        val json = post("InitiateAuth", request.asJson())
        return InitiateAuthResponse.from(json)
    }

    fun globalSignOut(accessToken: String) {
        val requestBody = JSONObject().put("AccessToken", accessToken)
        post("GlobalSignOut", requestBody)
    }

    fun respondToAuthChallenge(
        request: RespondToAuthChallengeRequest
    ): RespondToAuthChallengeResponse {
        val json = post("RespondToAuthChallenge", request.asJson())
        return RespondToAuthChallengeResponse.from(json)
    }

    private fun post(action: String, json: JSONObject): JSONObject {
        val input = json.toString().toByteArray()

        val url = URL(endpoint)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-amz-json-1.1")
        conn.setRequestProperty("amz-sdk-invocation-id", UUID.randomUUID().toString())
        conn.setRequestProperty("X-Amz-Target", "AWSCognitoIdentityProviderService.$action")
        conn.setRequestProperty("amz-sdk-request-id", "attempt=1; max=3")
        conn.setRequestProperty("Content-Length", input.size.toString())

        conn.doOutput = true

        conn.outputStream.write(input, 0, input.size)

        if (conn.responseCode < 200 || conn.responseCode > 399) {
            throw ResponseError(conn.responseCode, readStream(conn.errorStream))
        } else {
            val response = JSONObject(readStream(conn.inputStream))
            Log.i("Cognito", response.toString())
            return response
        }
    }

    private fun readStream(stream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(stream))
        val response = StringBuilder()
        var responseLine: String? = reader.readLine()
        while (responseLine != null) {
            response.append(responseLine.trim())
            responseLine = reader.readLine()
        }
        return response.toString()
    }

    class ResponseError(code: Int, message: String) : Exception(message)
}
