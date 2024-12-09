package com.example.estoquelegal

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data class representing a Type
data class Type(
    val id: String = "",
    val name: String = "",
    val creationDate: String = ""
)

class TypeController {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("types")

    // Function to create a new type
    fun createType(name: String, callback: (Boolean, String?) -> Unit) {
        val typeId = database.push().key ?: return callback(false, "Failed to generate key")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val creationDate = dateFormat.format(Date())
        val type = Type(id = typeId, name = name, creationDate = creationDate)
        database.child(typeId).setValue(type)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    // Function to retrieve all types
    fun getAllTypes(callback: (List<Type>?, String?) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val types = task.result.children.mapNotNull { it.getValue(Type::class.java) }
                callback(types, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }

    // Function to retrieve a type by its ID
    fun getType(typeId: String, callback: (Type?, String?) -> Unit) {
        database.child(typeId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val type = task.result.getValue(Type::class.java)
                callback(type, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }
}