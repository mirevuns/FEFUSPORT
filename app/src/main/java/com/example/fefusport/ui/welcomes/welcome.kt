package com.example.fefusport.ui.welcomes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fefusport.Main
import com.example.fefusport.R
import com.example.fefusport.data.preferences.SessionManager
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)
        if (sessionManager.getCurrentUserId() != null) {
            startActivity(Intent(this, Main::class.java))
            finish()
            return
        }

        setContentView(R.layout.welcome)

        val button1 = findViewById<MaterialButton>(R.id.button)
        button1.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<MaterialButton>(R.id.text_button)
        button2.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}
