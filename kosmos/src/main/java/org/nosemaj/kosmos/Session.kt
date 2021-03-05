package org.nosemaj.kosmos

sealed class Session() {
    class GuestSession() : Session()
    class AuthenticatedSession(
        val identityId: String,
        val credentials: Credentials
    ) : Session()

    data class Credentials(
        val accessKeyId: String,
        val secretKey: String,
        val sessionToken: String
    )
}
