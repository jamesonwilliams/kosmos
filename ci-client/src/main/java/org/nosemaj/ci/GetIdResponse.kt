package org.nosemaj.ci

import org.json.JSONObject

data class GetIdResponse(val identityId: String) {
    companion object {
        fun from(json: JSONObject): GetIdResponse {
            return GetIdResponse(json.getString("IdentityId"))
        }
    }
}
