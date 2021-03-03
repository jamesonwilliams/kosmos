package org.nosemaj.kosmos.cognito

data class AuthenticationResult(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String?,
    val expiresIn: Int,
    val tokenType: String
)
