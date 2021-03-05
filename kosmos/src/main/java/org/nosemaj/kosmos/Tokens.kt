package org.nosemaj.kosmos

sealed class Tokens {
    object InvalidTokens : Tokens()

    data class ValidTokens(
        val accessToken: String,
        val idToken: String
    ) : Tokens()
}
