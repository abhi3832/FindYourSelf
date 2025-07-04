package com.example.findyourself.view.screens.authScreens.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowFieldForInput(
    value: MutableState<String>,
    label: String,
    placeHolder: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector?,
    limit: Int = 30
)
{
    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = if (it.length <= limit) it else it.take(limit)},
        label = { Text(label, modifier = Modifier.alpha(0.7f), color = MaterialTheme.colorScheme.onBackground) },
        placeholder = { Text(placeHolder) },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        trailingIcon = {
            Text(
                text = "${value.value.length}/$limit",
                fontSize = 12.sp,
                color = if (value.value.length <= 50) Color.Gray else Color.Red,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
        )
    )
}