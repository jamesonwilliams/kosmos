package org.nosemaj.kosmos.storage

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.KeyScheme
import org.nosemaj.kosmos.storage.SecureCredentialStorage.Key.ACCESS_TOKEN
import org.nosemaj.kosmos.storage.SecureCredentialStorage.Key.EXPIRATION_EPOCH
import org.nosemaj.kosmos.storage.SecureCredentialStorage.Key.ID_TOKEN
import org.nosemaj.kosmos.storage.SecureCredentialStorage.Key.REFRESH_TOKEN
import org.nosemaj.kosmos.storage.SecureCredentialStorage.Key.TOKEN_TYPE
import java.util.concurrent.TimeUnit
import kotlin.math.max

class SecureCredentialStorage(context: Context) : CredentialStorage {
    private val masterKey = MasterKey.Builder(context, "kosmos")
        .setKeyScheme(KeyScheme.AES256_GCM)
        .build()
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "kosmos",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun clear() {
        prefs.edit().clear().commit()
    }

    override fun isEmpty(): Boolean {
        return !prefs.contains(EXPIRATION_EPOCH.name)
    }

    override fun accessToken(token: String) {
        store(ACCESS_TOKEN, token)
    }

    override fun accessToken(): String {
        return getString(ACCESS_TOKEN)
    }

    override fun idToken(token: String) {
        store(ID_TOKEN, token)
    }

    override fun idToken(): String {
        return getString(ID_TOKEN)
    }

    override fun refreshToken(token: String?) {
        if (token != null) {
            store(REFRESH_TOKEN, token)
        } else {
            prefs.edit().remove(REFRESH_TOKEN.name).commit()
        }
    }

    override fun refreshToken(): String {
        return getString(REFRESH_TOKEN)
    }

    override fun expiresIn(period: Int) {
        store(EXPIRATION_EPOCH, now() + period)
    }

    // TODO: feels like this logic probably belongs a level higher in abstraction.
    override fun isExpired(): Boolean {
        val gracePeriod = TimeUnit.MINUTES.toSeconds(3)
        val expirationEpoch = prefs.getLong(EXPIRATION_EPOCH.name, 0)
        val safelyBeforeExpiration = max(0, expirationEpoch - gracePeriod)
        Log.i("CredentialsStorage", "now() = ${now()}, but deadline = $safelyBeforeExpiration")
        return now() > safelyBeforeExpiration
    }

    private fun now(): Long {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    }

    override fun tokenType(type: String) {
        return store(TOKEN_TYPE, type)
    }

    override fun tokenType(): String {
        return getString(TOKEN_TYPE)
    }

    private fun store(key: Key, value: String) {
        prefs.edit()
            .putString(key.name, value)
            .commit()
    }

    private fun store(key: Key, value: Long) {
        prefs.edit()
            .putLong(key.name, value)
            .commit()
    }

    private fun getString(key: Key): String {
        return prefs.getString(key.name, null)!!
    }

    enum class Key {
        ACCESS_TOKEN,
        ID_TOKEN,
        REFRESH_TOKEN,
        EXPIRATION_EPOCH,
        TOKEN_TYPE
    }
}