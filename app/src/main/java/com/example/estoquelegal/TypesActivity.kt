package com.example.estoquelegal

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import java.text.SimpleDateFormat
import java.util.Locale


class TypesActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var labelAmount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tableTypes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_types)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        labelAmount = findViewById(R.id.labelAmount_Type)
        buttonRegister = findViewById(R.id.buttonRegister_Type)
        progressBar = findViewById(R.id.progressBar_Type)
        tableTypes = findViewById(R.id.tableTypes_Type)

        buttonRegister.setOnClickListener {
            addProductType()
        }
        updateTable()
    }

    private fun addProductType() {
        //name input dialog
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        dialog.setView(input)
        dialog.setTitle("Register new product type")
        dialog.setMessage("Type name:")
        dialog.setPositiveButton("Create") { dialog, which ->
            val name = input.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                buttonRegister.isEnabled = false
                labelAmount.text = "Creating type..."
                progressBar.isGone = false
                val typeController = TypeController()
                typeController.createType(name) { success, message ->
                    if (success) {
                        Toast.makeText(this, "Type created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to create type: $message", Toast.LENGTH_SHORT).show()
                    }
                    buttonRegister.isEnabled = true
                    progressBar.isGone = true
                    updateTable()
                }
            }
        }
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        dialog.show()
    }

    private fun updateTable() {
        tableTypes.removeAllViews()
        val typeController = TypeController()
        typeController.getAllTypes { types, message ->
            if (types != null) {
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("dd-MM-yyyy  HH:mm:ss", Locale.getDefault())
                for (type in types) {
                    val typeRow = layoutInflater.inflate(R.layout.type_row, tableTypes, false)
                    val labelName = typeRow.findViewById<TextView>(R.id.labelTypeName)
                    val labelCreationDate = typeRow.findViewById<TextView>(R.id.labelCreation)
                    labelName.text = type.name
                    val date = inputDateFormat.parse(type.creationDate)
                    labelCreationDate.text = outputDateFormat.format(date)
                    tableTypes.addView(typeRow)
                }
                updateAmount()
            } else {
                Toast.makeText(this, "Failed to retrieve types: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAmount() {
        val typeController = TypeController()
        typeController.getAllTypes { types, message ->
            if (types != null) {
                labelAmount.text = "Amount of types: ${types.size}"
            } else {
                Toast.makeText(this, "Failed to retrieve types: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}