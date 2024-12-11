package com.example.estoquelegal

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class CreateProductActivity : AppCompatActivity() {
    private lateinit var inputName: TextInputEditText
    private lateinit var spinnerType: Spinner
    private lateinit var inputPrice: TextInputEditText
    private lateinit var buttonCreate: Button
    private lateinit var buttonCancel: Button
    private lateinit var progressBar: ProgressBar
    private val typeController = TypeController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputName = findViewById(R.id.inputName_CreateProd)
        spinnerType = findViewById(R.id.spinnerType_CreateProd)
        inputPrice = findViewById(R.id.inputPrice_CreateProd)
        buttonCreate = findViewById(R.id.buttonCreate_CreateProd)
        buttonCancel = findViewById(R.id.buttonCancel_CreateProd)
        progressBar = findViewById(R.id.progressBar_CreateProd)

        typeController.getAllTypes { types, error ->
            if (types != null) {
                val typeNames = types.map { it.name }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerType.adapter = adapter
            } else {
                error?.let { println("Error retrieving types: $it") }
            }
        }

        buttonCancel.setOnClickListener {
            finish()
        }
        buttonCreate.setOnClickListener {
            val name = inputName.text.toString()
            val type = spinnerType.selectedItem.toString()
            val price = inputPrice.text.toString().toDoubleOrNull()
            if (name.isNotBlank() && price != null) {
                progressBar.visibility = ProgressBar.VISIBLE
                buttonCreate.isEnabled = false
                buttonCancel.isEnabled = false
                val product = Product("",name, type, price)
                ProductController().registerProduct(product) { _, error ->
                    if (error != null) {
                        println("Error creating product: $error")
                    }
                    progressBar.visibility = ProgressBar.INVISIBLE
                    buttonCreate.isEnabled = true
                    buttonCancel.isEnabled = true
                    finish()
                }
            }
        }
    }
}