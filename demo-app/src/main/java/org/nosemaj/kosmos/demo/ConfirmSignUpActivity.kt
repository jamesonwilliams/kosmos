package org.nosemaj.kosmos.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.nosemaj.kosmos.demo.databinding.ActivityConfirmSignUpBinding

class ConfirmSignUpActivity : AppCompatActivity() {
    private lateinit var view: ActivityConfirmSignUpBinding
    private val auth get() = (applicationContext as KosmosDemoApp).auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityConfirmSignUpBinding.inflate(layoutInflater)
        setContentView(view.root)
        val username = intent.getStringExtra("username")!!
        view.submitButton.setOnClickListener { submitConfirmationCode(username) }
    }

    private fun submitConfirmationCode(username: String) {
        lifecycleScope.launch {
            val code = view.codeEntry.text.toString()
            withContext(Dispatchers.IO) {
                auth.confirmRegistration(username, code)
            }
            goToSignIn(this@ConfirmSignUpActivity, username)
        }
    }
}

fun goToConfirmSignUp(source: Activity, username: String) {
    val confirmSignUpIntent = Intent(source, ConfirmSignUpActivity::class.java)
    confirmSignUpIntent.putExtra("username", username)
    source.startActivity(confirmSignUpIntent)
}
