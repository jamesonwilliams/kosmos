package org.nosemaj.kosmos.demo

import android.app.Application
import org.nosemaj.kosmos.Auth

class KosmosDemoApp : Application() {
    lateinit var auth: Auth

    override fun onCreate() {
        super.onCreate()
        auth = Auth(
            context = this,
            poolId = resources.getString(R.string.pool_id),
            clientId = resources.getString(R.string.client_id),
            clientSecret = resources.getString(R.string.client_secret)
        )
    }
}
