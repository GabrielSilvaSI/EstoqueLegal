package com.example.estoquelegal

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar: Toolbar
    private lateinit var buttonProducts: Button
    private lateinit var buttonSuppliers: Button
    private lateinit var buttonClients: Button
    private lateinit var buttonProductTypes: Button
    private lateinit var buttonUsers: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance() // Initialize auth

        val user = auth.currentUser
        verifyAuth(user)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle("💼 " + user?.email)
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_signout) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.signout))
                builder.setMessage(getString(R.string.signout_confirmation))
                builder.setIcon(R.drawable.baseline_logout_24)
                builder.setNegativeButton(getString(R.string.no), null)
                builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    signOut()
                }
                builder.show()

                return@setOnMenuItemClickListener true
            }
            false
        }

        buttonProducts = findViewById(R.id.buttonProducts_Home)
        buttonSuppliers = findViewById(R.id.buttonSuppliers_Home)
        buttonClients = findViewById(R.id.buttonClients_Home)
        buttonProductTypes = findViewById(R.id.buttonProductTypes_Home)
        buttonUsers = findViewById(R.id.buttonUsers_Home)

        buttonUsers.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }
        buttonProductTypes.setOnClickListener {
            val intent = Intent(this, TypesActivity::class.java)
            startActivity(intent)
        }
        buttonSuppliers.setOnClickListener {
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
        }
        buttonProducts.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun verifyAuth(user: FirebaseUser?) {
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}