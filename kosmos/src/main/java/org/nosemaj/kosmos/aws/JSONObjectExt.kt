package org.nosemaj.kosmos.aws

import org.json.JSONObject

fun JSONObject.maybeString(key: String): String? {
    return if (isNull(key)) null else getString(key)
}
