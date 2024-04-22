package com.example.expenserecordingapp.data.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

lateinit private var myRef : DatabaseReference
private var databaseLink = "https://cash-vault-d86fc-default-rtdb.asia-southeast1.firebasedatabase.app/"

fun ConnectToFirebase(){
    val database = FirebaseDatabase.getInstance(databaseLink)
    myRef = database.getReference("message")
}

fun SetDataDatabase(){
    myRef.setValue("Hello, World!")
}

fun GetDataDatabase(){
    myRef.addValueEventListener(object: ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = snapshot.getValue<String>()
            Log.d(TAG, "Value is: " + value)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    })
}