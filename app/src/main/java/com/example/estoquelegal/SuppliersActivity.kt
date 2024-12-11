package com.example.estoquelegal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SuppliersActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var tableSuppliers: LinearLayout
    private lateinit var labelAmount: TextView

    companion object {
        const val REQUEST_CODE_CREATE_SUPPLIER = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_suppliers)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tableSuppliers = findViewById(R.id.tableSuppliers_Suppliers)
        labelAmount = findViewById(R.id.labelAmount_Supplier)

        buttonRegister = findViewById(R.id.buttonRegister_Supplier)
        buttonRegister.setOnClickListener {
            val intent = Intent(this, CreateSupplierActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREATE_SUPPLIER)
        }
        updateTable()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_SUPPLIER && resultCode == RESULT_OK) {
            updateTable()
        }
    }

    private fun updateTable() {
        SupplierController().getAllSuppliers { suppliers, error ->
            if (suppliers != null) {
                tableSuppliers.removeAllViews()
                suppliers.forEach { supplier ->
                    val row = layoutInflater.inflate(R.layout.supplier_row, tableSuppliers, false)
                    row.findViewById<TextView>(R.id.labelId_suprow).text = supplier.id
                    row.findViewById<TextView>(R.id.labelname_suprow).text = supplier.name
                    row.findViewById<TextView>(R.id.labelphone_suprow).text = supplier.phone
                    tableSuppliers.addView(row)
                }
            }
            labelAmount.text = "Amount users: ${suppliers?.count()}"
        }
    }
}