package com.example.findyourself.screens.authScreens.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.dataClasses.Country

@Composable
fun MobileNumberPrefixField(
    showDialog: MutableState<Boolean>,
    selectedCountry: MutableState<Country>,
    selectedCountryCode: MutableState<String>
){

        val interactionSource = remember { MutableInteractionSource() }

        Box(modifier = Modifier.border(1.dp, color = MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(8.dp)).clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { showDialog.value = !showDialog.value }))
        {

            TextField(
                readOnly = true,
                enabled = false,
                value = selectedCountryCode.value,
                onValueChange = {},
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground,textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth(0.2f)
                    .background(color = MaterialTheme.colorScheme.background)
                    .clip(shape = RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.22f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.22f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
}