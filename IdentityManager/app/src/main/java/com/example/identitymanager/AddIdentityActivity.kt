package com.example.identitymanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddIdentityActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_identity)

        nicknameEditText = findViewById(R.id.nicknameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        notesEditText = findViewById(R.id.notesEditText)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val nickname = nicknameEditText.text.toString()
            val email = emailEditText.text.toString()
            val notes = notesEditText.text.toString()

            if (nickname.isBlank() || email.isBlank()) {
                // Basic validation: Show a Toast or error message
                // For simplicity, just logging or returning if fields are empty
                if (nickname.isBlank()) nicknameEditText.error = "Nickname cannot be empty"
                if (email.isBlank()) emailEditText.error = "Email cannot be empty"
                return@setOnClickListener
            }

            val newIdentity = Identity(
                nickname = nickname,
                email = email,
                notes = notes.takeIf { it.isNotBlank() } // Only set notes if not blank
            )

            val resultIntent = Intent()
            resultIntent.putExtra("NEW_IDENTITY", newIdentity)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
