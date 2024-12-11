package com.example.estoquelegal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProductsActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var labelAmount: TextView
    private lateinit var tableProducts: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonRegister = findViewById(R.id.buttonRegister_Products)
        labelAmount = findViewById(R.id.labelAmount_Products)
        tableProducts = findViewById(R.id.tableProducts_Products)

        buttonRegister.setOnClickListener {
            val intent = Intent(this, CreateProductActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateProducts()
    }

    private fun updateProducts() {
        val products = ProductController().getAllProducts( { products, error ->
            if (products != null) {
                labelAmount.text = "Amount products: ${products.size}"
                tableProducts.removeAllViews()
                products.forEach { product ->
                    val view = layoutInflater.inflate(R.layout.product_row, tableProducts, false)
                    view.findViewById<TextView>(R.id.labelId_RowProd).text = product.id
                    view.findViewById<TextView>(R.id.labelName_RowProd).text = product.name
                    view.findViewById<TextView>(R.id.labelType_RowProd).text = product.type
                    view.findViewById<TextView>(R.id.labelPrice_RowProd).text = product.unitPrice.toString()
                    tableProducts.addView(view)
                }
            } else {
                error?.let { println("Error retrieving products: $it") }
            }
        })
    }
}