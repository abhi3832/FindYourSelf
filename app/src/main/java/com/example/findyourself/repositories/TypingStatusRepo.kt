package com.example.findyourself.repositories

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypingStatusRepository @Inject constructor(private val db : FirebaseDatabase) {


    fun setTypingStatus(chatId: String, userId: String, isTyping: Boolean) {
        db.getReference("typingStatus")
            .child(chatId)
            .child(userId)
            .setValue(isTyping)
    }

    fun observeTypingStatus(chatId: String, onUpdate: (Map<String, Boolean>) -> Unit): ValueEventListener {
        val ref = db.getReference("typingStatus").child(chatId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typingMap = (snapshot.value as? Map<String, Boolean>) ?: emptyMap()
                onUpdate(typingMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TypingStatus", "Error: ${error.message}")
            }
        }

        ref.addValueEventListener(listener)
        return listener
    }

    fun removeListener(chatId: String, listener: ValueEventListener) {
        db.getReference("typingStatus").child(chatId).removeEventListener(listener)
    }
}
