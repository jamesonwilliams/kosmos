package org.nosemaj.kosmos.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.nosemaj.kosmos.Session.ValidSession
import org.nosemaj.kosmos.demo.databinding.ActivityCredentialsStatusBinding

class CredentialsStatusPage : AppCompatActivity() {
    private lateinit var view: ActivityCredentialsStatusBinding
    private val auth get() = (applicationContext as KosmosDemoApp).auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityCredentialsStatusBinding.inflate(layoutInflater)
        setContentView(view.root)
        view.signOutButton.setOnClickListener {
            signOut()
        }
        displayMessage(intent.getStringExtra("message"))
        navigate()
    }

    private fun signOut() {
        lifecycleScope.launch {
            auth.signOut()
            view.tokenInfo.visibility = INVISIBLE
            displayMessage("Signed out!")
        }
    }

    private fun navigate() {
        lifecycleScope.launch(Dispatchers.IO) {
            when (val session = auth.session()) {
                is ValidSession -> displayTokens(session)
                else -> goToSignIn(source = this@CredentialsStatusPage)
            }
        }
    }

    private fun displayMessage(text: String?) {
        if (text != null) {
            view.message.text = text
            view.message.visibility = VISIBLE
        } else {
            view.message.visibility = INVISIBLE
        }
    }

    private fun displayTokens(session: ValidSession) {
        view.accessToken.text = session.accessToken
        view.idToken.text = session.idToken
        view.tokenInfo.visibility = VISIBLE
    }
}

fun goToCredentialsStatus(origin: Activity, message: String) {
    val intent = Intent(origin, CredentialsStatusPage::class.java)
    intent.putExtra("message", message)
    origin.startActivity(intent)
}
