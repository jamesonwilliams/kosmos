package org.nosemaj.kosmos.cip

data class AuthenticationResult(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String?,
    val expiresIn: Int,
    val tokenType: String
)
