package com.example.estoquelegal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    lateinit var inputEmail: TextInputEditText
    lateinit var inputPassword: TextInputEditText
    lateinit var buttonSignin: Button
    lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputEmail = findViewById(R.id.loginInputEmail)
        inputPassword = findViewById(R.id.loginInputPassword)
        buttonSignin = findViewById(R.id.loginButtonSignin)
        progressBar = findViewById(R.id.loginProgressBar)

        auth = Firebase.auth

        buttonSignin.setOnClickListener {
            if (inputEmail.text.toString().isEmpty()) {
                inputEmail.error = getString(R.string.email_required)
                return@setOnClickListener
            }
            if (inputPassword.text.toString().isEmpty()) {
                inputPassword.error = getString(R.string.password_required)
                return@setOnClickListener
            }
            authRequest(inputEmail.text.toString(), inputPassword.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun authRequest(email: String, password: String) {
        progressBar.visibility = ProgressBar.VISIBLE
        buttonSignin.visibility = Button.INVISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, getString(R.string.login_success),
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    inputEmail.error = getString(R.string.invalid_credentials)
                    inputPassword.error = getString(R.string.invalid_credentials)
                    Toast.makeText(baseContext, task.exception?.message,
                        Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = ProgressBar.INVISIBLE
                buttonSignin.visibility = Button.VISIBLE
            }

    }
}