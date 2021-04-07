package org.nosemaj.kosmos.cip

import org.json.JSONObject

data class IntrospectTokenResponse(val active: Boolean) {
    companion object {
        fun from(json: JSONObject): IntrospectTokenResponse {
            return IntrospectTokenResponse(json.getBoolean("Active"))
        }
    }
}
