package com.example.estoquelegal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class User(
    val name: String = "",
    val email: String = "",
    val id: String = "",
    val phone: String = "",
    val active: Boolean = true
)

class UserController {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun createUser(email: String, password: String, user: User, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.child(user.id).setValue(user)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                callback(true, null)
                            } else {
                                callback(false, dbTask.exception?.message)
                            }
                        }
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun getUser(userId: String, callback: (User?, String?) -> Unit) {
        database.child(userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.getValue(User::class.java)
                callback(user, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }

    fun updateUser(userId: String, user: User, callback: (Boolean, String?) -> Unit) {
        database.child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun getAllUsers(callback: (List<User>?, String?) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val users = task.result.children.mapNotNull { it.getValue(User::class.java) }
                callback(users, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }
}