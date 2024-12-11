package com.example.estoquelegal

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class Supplier(
    val id: String = "",
    val name: String = "",
    val phone: String = ""
)

class SupplierController {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("suppliers")

    // Function to create a new supplier with a given ID
    fun createSupplier(supplier: Supplier, callback: (Boolean, String?) -> Unit) {
        val supplierId = supplier.id
        database.child(supplierId).setValue(supplier)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    // Function to retrieve all suppliers
    fun getAllSuppliers(callback: (List<Supplier>?, String?) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val suppliers = task.result.children.mapNotNull { it.getValue(Supplier::class.java) }
                callback(suppliers, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }
}