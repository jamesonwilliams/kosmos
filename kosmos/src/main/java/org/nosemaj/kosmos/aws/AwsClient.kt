package org.nosemaj.kosmos.aws

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import org.json.JSONObject

class AwsClient(private val serviceId: String, private val endpoint: String) {
    fun post(action: String, json: JSONObject): JSONObject {
        val input = json.toString().encodeToByteArray()
        Log.i("REQUEST", json.toString(2))

        val url = URL(endpoint)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-amz-json-1.1")
        conn.setRequestProperty("amz-sdk-invocation-id", UUID.randomUUID().toString())
        conn.setRequestProperty("X-Amz-Target", "$serviceId.$action")
        conn.setRequestProperty("amz-sdk-request-id", "attempt=1; max=3")
        conn.setRequestProperty("Content-Length", input.size.toString())

        conn.doOutput = true

        conn.outputStream.write(input, 0, input.size)

        if (conn.responseCode < 200 || conn.responseCode > 399) {
            val value = readStream(conn.errorStream)
            Log.i("Error", value)
            throw ResponseError(value)
        } else {
            val foo = JSONObject(readStream(conn.inputStream))
            Log.i("Response", foo.toString(2))
            return foo
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

    // TODO: buff up this error class 💪
    // Sealed class with status codes, etc.?
    class ResponseError(message: String) : Exception(message)
}
