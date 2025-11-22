package com.example.fefusport.ui.welcomes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.fefusport.Main
import com.example.fefusport.R
import com.example.fefusport.data.preferences.SessionManager
import com.example.fefusport.ui.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Login : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        sessionManager = SessionManager(this)

        val loginLayout = findViewById<TextInputLayout>(R.id.loginInputLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val loginInput = findViewById<TextInputEditText>(R.id.loginEditText)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordEditText)

        findViewById<ImageButton>(R.id.back).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<MaterialButton>(R.id.button).setOnClickListener {
            loginLayout.error = null
            passwordLayout.error = null

            val login = loginInput.text?.toString()?.trim().orEmpty()
            val password = passwordInput.text?.toString().orEmpty()

            if (login.isEmpty()) {
                loginLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }

            userViewModel.login(login, password) { user ->
                if (user == null) {
                    passwordLayout.error = getString(R.string.invalid_credentials)
                    Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show()
                } else {
                    sessionManager.setCurrentUserId(user.id)
                    openMain()
                }
            }
        }
    }

    private fun openMain() {
        val intent = Intent(this, Main::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}