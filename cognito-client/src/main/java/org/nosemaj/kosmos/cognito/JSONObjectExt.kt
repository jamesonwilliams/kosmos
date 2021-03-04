package org.nosemaj.kosmos.cognito

import org.json.JSONObject

fun JSONObject.maybeString(key: String): String? {
    return if (isNull(key)) null else optString(key, null)
}
