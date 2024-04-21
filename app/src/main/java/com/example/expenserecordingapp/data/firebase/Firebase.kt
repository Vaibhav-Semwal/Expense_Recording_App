package com.example.expenserecordingapp.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.database.database

fun ConnectToFirebase(){
    val database = Firebase.database
    val myRef = database.getReference("message")

    myRef.setValue("Hello, World!")
}