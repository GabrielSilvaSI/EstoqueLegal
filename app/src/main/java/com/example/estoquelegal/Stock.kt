package com.example.estoquelegal
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class StockItem(
    val id : String = "",
    val quantity: Int = 0,
    val supplier: String = ""
)



class StockController {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("stockItems")

    // Function to register a new stock item with a provided ID
    fun registerStockItem(stockItem: StockItem, callback: (Boolean, String?) -> Unit) {
        val stockItemId = stockItem.id // Use the provided ID
        database.child(stockItemId).setValue(stockItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    // Function to retrieve all stock items
    fun getAllStockItems(callback: (List<StockItem>?, String?) -> Unit) {
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val stockItems = task.result.children.mapNotNull { it.getValue(StockItem::class.java) }
                callback(stockItems, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }
}