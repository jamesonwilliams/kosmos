package org.nosemaj.kosmos

sealed class Session(val signedIn: Boolean) {
    class InvalidSession : Session(false)

    data class ValidSession(
        val accessToken: String,
        val idToken: String
    ) : Session(true)
}
