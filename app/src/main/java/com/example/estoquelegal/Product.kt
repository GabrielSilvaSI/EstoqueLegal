package com.example.estoquelegal

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


data class Product(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val unitPrice: Double = 0.0
)


class ProductController {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

    // Function to register a new product
    fun registerProduct(product: Product, callback: (Boolean, String?) -> Unit) {
        val productId = database.push().key // Generate a unique key
        if (productId != null) {
            val productWithId = product.copy(id = productId) // Create a new product with the generated ID
            database.child(productId).setValue(productWithId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true, null)
                    } else {
                        callback(false, task.exception?.message)
                    }
                }
        } else {
            callback(false, "Failed to generate product ID")
        }
    }

    // Function to retrieve all products
    fun getAllProducts(callback: (List<Product>?, String?) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val products = task.result.children.mapNotNull { it.getValue(Product::class.java) }
                callback(products, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }
}