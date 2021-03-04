package org.nosemaj.kosmos.demo

import android.app.Application
import org.nosemaj.kosmos.Auth
import org.nosemaj.kosmos.storage.SecureCredentialStorage

class KosmosDemoApp : Application() {
    lateinit var auth: Auth

    override fun onCreate() {
        super.onCreate()
        auth = Auth(
            poolId = "",
            clientId = "",
            clientSecret = "",
            credentialStorage = SecureCredentialStorage(this)
        )
    }
}
