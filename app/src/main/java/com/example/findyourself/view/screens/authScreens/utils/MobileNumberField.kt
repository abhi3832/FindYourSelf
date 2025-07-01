package com.example.findyourself.view.screens.authScreens.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findyourself.model.Country

@Composable
fun MobileNumberField(
    showDialog: MutableState<Boolean>,
    value: MutableState<String>,
    selectedCountry: MutableState<Country>,
    selectedCountryCode: MutableState<String>
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    )
    {
        MobileNumberPrefixField(showDialog, selectedCountry, selectedCountryCode)
        Spacer(modifier = Modifier.width(8.dp))


        ShowFieldForInput(
            value = value,
            label = "Mobile Number",
            placeHolder = "Enter your phone",
            leadingIcon = Icons.Default.PhoneAndroid,
            trailingIcon = null,
            limit = 11
        )

    }
}

