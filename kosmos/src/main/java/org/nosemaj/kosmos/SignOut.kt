package org.nosemaj.kosmos

import org.nosemaj.kosmos.cip.CipClient
import org.nosemaj.kosmos.storage.TokenStorage

class SignOut(
    private val client: CipClient,
    private val tokenStorage: TokenStorage
) {
    fun execute() {
        val accessToken = tokenStorage.accessToken()
        tokenStorage.clear()
        client.globalSignOut(accessToken = accessToken)
    }
}
