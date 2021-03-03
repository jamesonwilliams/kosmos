package org.nosemaj.kosmos.cognito

data class CodeDeliveryDetails(
    val destination: String,
    val attributeName: String,
    val deliveryMedium: String
)
