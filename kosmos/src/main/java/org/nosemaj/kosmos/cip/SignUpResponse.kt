package org.nosemaj.kosmos.cip

import org.json.JSONObject

data class SignUpResponse(
    val userConfirmed: Boolean,
    val codeDeliveryDetails: CodeDeliveryDetails,
    val userSub: String
) {
    companion object {
        fun from(json: JSONObject): SignUpResponse {
            val codeDeliveryJson = json.getJSONObject("CodeDeliveryDetails")
            return SignUpResponse(
                userConfirmed = json.getBoolean("UserConfirmed"),
                userSub = json.getString("UserSub"),
                codeDeliveryDetails = CodeDeliveryDetails(
                    attributeName = codeDeliveryJson.getString("AttributeName"),
                    deliveryMedium = codeDeliveryJson.getString("DeliveryMedium"),
                    destination = codeDeliveryJson.getString("Destination")
                )
            )
        }
    }
}
