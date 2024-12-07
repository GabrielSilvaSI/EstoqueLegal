package com.example.estoquelegal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UsersActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var tableUsers: LinearLayout
    private lateinit var labelAmount: TextView

    companion object {
        const val REQUEST_CODE_CREATE_USER = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_users)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonRegister = findViewById(R.id.buttonRegister_Users)
        buttonRegister.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREATE_USER)
        }

        tableUsers = findViewById(R.id.tableUsers_Users)
        labelAmount = findViewById(R.id.labelAmount_Users)

        updateTable()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_USER && resultCode == RESULT_OK) {
            updateTable()
        }
    }

    private fun updateTable() {
        UserController().getAllUsers { users, error ->
            if (users != null) {
                tableUsers.removeAllViews()
                users.forEach { user ->
                    val row = layoutInflater.inflate(R.layout.user_row, tableUsers, false)
                    row.findViewById<TextView>(R.id.labelName).text = user.name
                    row.findViewById<TextView>(R.id.labelEmail).text = user.email
                    row.findViewById<TextView>(R.id.labelId).text = user.id
                    row.findViewById<TextView>(R.id.labelPhone).text = user.phone
                    row.findViewById<Switch>(R.id.switchStatus).isChecked = user.active
                    row.findViewById<Switch>(R.id.switchStatus).text = if (user.active) "Active" else "Inactive"
                    tableUsers.addView(row)
                }
            }
            labelAmount.text = "Amount users: ${users?.count()}"
        }
    }
}