package com.example.fefusport.ui.welcomes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.fefusport.Main
import com.example.fefusport.R
import com.example.fefusport.data.preferences.SessionManager
import com.example.fefusport.model.User
import com.example.fefusport.ui.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Registration : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        sessionManager = SessionManager(this)

        findViewById<ImageButton>(R.id.back).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val loginLayout = findViewById<TextInputLayout>(R.id.loginInputLayout)
        val nicknameLayout = findViewById<TextInputLayout>(R.id.nicknameInputLayout)
        val ageLayout = findViewById<TextInputLayout>(R.id.ageInputLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val confirmLayout = findViewById<TextInputLayout>(R.id.confirmPasswordInputLayout)

        val loginInput = findViewById<TextInputEditText>(R.id.loginInputEditText)
        val nicknameInput = findViewById<TextInputEditText>(R.id.nicknameInputEditText)
        val ageInput = findViewById<TextInputEditText>(R.id.ageInputEditText)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInputEditText)
        val confirmInput = findViewById<TextInputEditText>(R.id.confirmPasswordInputEditText)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)

        findViewById<MaterialButton>(R.id.button).setOnClickListener {
            loginLayout.error = null
            nicknameLayout.error = null
            ageLayout.error = null
            passwordLayout.error = null
            confirmLayout.error = null

            val login = loginInput.text?.toString()?.trim().orEmpty()
            val nickname = nicknameInput.text?.toString()?.trim().orEmpty()
            val ageValue = ageInput.text?.toString()?.trim().orEmpty()
            val password = passwordInput.text?.toString().orEmpty()
            val confirm = confirmInput.text?.toString().orEmpty()

            if (login.isEmpty()) {
                loginLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            if (nickname.isEmpty()) {
                nicknameLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            val age = ageValue.toIntOrNull()
            if (age == null) {
                ageLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            if (password != confirm) {
                confirmLayout.error = getString(R.string.passwords_not_match)
                return@setOnClickListener
            }

            val gender = when (genderGroup.checkedRadioButtonId) {
                R.id.maleRadio -> "Мужской"
                R.id.femaleRadio -> "Женский"
                R.id.otherRadio -> "Другое"
                else -> "Не указан"
            }

            val newUser = User(
                login = login,
                password = password,
                nickname = nickname,
                gender = gender,
                age = age
            )

            userViewModel.register(newUser) { result ->
                result.onSuccess { id ->
                    sessionManager.setCurrentUserId(id)
                    Toast.makeText(this, "Добро пожаловать, $nickname!", Toast.LENGTH_SHORT).show()
                    openMain()
                }.onFailure {
                    loginLayout.error = getString(R.string.user_exists)
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