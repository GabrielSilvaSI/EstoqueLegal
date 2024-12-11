package com.example.estoquelegal

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class CreateSupplierActivity : AppCompatActivity() {
    private lateinit var buttonCancel: Button
    private lateinit var buttonCreate: Button
    private lateinit var inputId: TextInputEditText
    private lateinit var inputName: TextInputEditText
    private lateinit var inputPhone: TextInputEditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_supplier)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonCancel = findViewById(R.id.buttonCancel_CreateSup)
        buttonCreate = findViewById(R.id.buttonCreate_CreateSup)
        inputId = findViewById(R.id.inputId_CreateSup)
        inputName = findViewById(R.id.inputName_CreateSup)
        inputPhone = findViewById(R.id.inputPhone_CreateSup)
        progressBar = findViewById(R.id.progressBar_CreateSup)


        buttonCancel.setOnClickListener {
            finish()
        }

        buttonCreate.setOnClickListener {
            val id = inputId.text.toString()
            val name = inputName.text.toString()
            val phone = inputPhone.text.toString()
            val supplier = Supplier(id, name, phone)

            progressBar.visibility = ProgressBar.VISIBLE
            buttonCreate.isEnabled = false
            SupplierController().createSupplier(supplier) { success, message ->
                if (success) {
                    Toast.makeText(baseContext, "Fornecedor registrado com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Erro ao registrar fornecedor: $message", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = ProgressBar.INVISIBLE
                buttonCreate.isEnabled = true
            }
        }
    }
}