package com.example.estoquelegal

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CreateUserActivity : AppCompatActivity() {
    private lateinit var buttonCancel: Button
    private lateinit var buttonCreate: Button
    private lateinit var inputName: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var inputId: TextInputEditText
    private lateinit var inputPhone: TextInputEditText
    private lateinit var switchActive: Switch
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputName = findViewById(R.id.inputName_CreateUser)
        inputEmail = findViewById(R.id.inputEmail_CreateUser)
        inputPassword = findViewById(R.id.inputPassword_CreateUser)
        inputId = findViewById(R.id.inputId_CreateUser)
        inputPhone = findViewById(R.id.inputPhone_CreateUser)
        switchActive = findViewById(R.id.switchActive_CreateUser)
        progressBar = findViewById(R.id.progressBar_CreateUser)

        buttonCancel = findViewById(R.id.buttonCancel_CreateUser)
        buttonCancel.setOnClickListener {
            finish()
        }

        buttonCreate = findViewById(R.id.buttonCreate_CreateUser)
        buttonCreate.setOnClickListener {
            val name = inputName.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val id = inputId.text.toString()
            val phone = inputPhone.text.toString()
            val active = switchActive.isChecked

            val user = User(name, email, id, phone, active)
            buttonCreate.visibility = Button.INVISIBLE
            progressBar.isVisible = true
            UserController().createUser(email, password, user) { success, message ->
                if (success) {
                    Toast.makeText(baseContext, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Erro ao criar usuário: $message", Toast.LENGTH_SHORT).show()
                }
                buttonCreate.visibility = Button.VISIBLE
                progressBar.isVisible = false
            }
        }
    }
}