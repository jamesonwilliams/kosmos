package org.nosemaj.kosmos.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.nosemaj.kosmos.Registration.ConfirmedRegistration
import org.nosemaj.kosmos.Registration.UnconfirmedRegistration
import org.nosemaj.kosmos.demo.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var view: ActivitySignUpBinding
    private val auth get() = (applicationContext as KosmosDemoApp).auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(view.root)
        view.submitButton.setOnClickListener { submitSignIn() }
    }

    private fun submitSignIn() {
        lifecycleScope.launch {
            val username = view.username.text.toString()
            val password = view.password.text.toString()
            val attributes = mapOf("email" to view.email.text.toString())
            val registration = withContext(Dispatchers.IO) {
                auth.registerUser(username, password, attributes)
            }
            when (registration) {
                is ConfirmedRegistration -> goToSignIn(this@SignUpActivity, username)
                is UnconfirmedRegistration -> goToConfirmSignUp(this@SignUpActivity, username)
            }
        }
    }
}

fun goToSignUp(source: Activity) {
    val signUpIntent = Intent(source, SignUpActivity::class.java)
    source.startActivity(signUpIntent)
}
