package com.example.expenserecordingapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.expenserecordingapp.R

val Pacifico = FontFamily(
    Font(R.font.pacifico)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Pacifico,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Pacifico,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Pacifico,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    )
)