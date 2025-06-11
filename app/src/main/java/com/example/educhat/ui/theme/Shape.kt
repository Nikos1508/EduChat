package com.example.educhat.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(50.dp),
    medium = RoundedCornerShape(bottomStart = 0.dp, topEnd = 0.dp, topStart = 16.dp, bottomEnd = 16.dp),
    extraLarge = RoundedCornerShape(100.dp)
)