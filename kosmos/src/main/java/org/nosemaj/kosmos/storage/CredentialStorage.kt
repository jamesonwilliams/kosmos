package org.nosemaj.kosmos.storage

interface CredentialStorage {

    fun clear()

    fun isEmpty(): Boolean

    fun accessToken(token: String)

    fun idToken(token: String)

    fun refreshToken(token: String?)

    fun expiresIn(period: Int)

    fun isExpired(): Boolean

    fun tokenType(type: String)

    fun accessToken(): String

    fun idToken(): String

    fun refreshToken(): String

    fun tokenType(): String
}
