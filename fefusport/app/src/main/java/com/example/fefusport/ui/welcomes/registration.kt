package com.example.fefusport.ui.welcomes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.ImageButton
import com.example.fefusport.R

class Registration : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        val button = findViewById<ImageButton>(R.id.back)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}