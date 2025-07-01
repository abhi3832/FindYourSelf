package com.example.findyourself.view.screens.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.model.Country

@Composable
fun AlertDialogForShowingCountries(
    openAlertDialog: MutableState<Boolean>,
    countryList: List<Country>,
    selectedCountry: MutableState<Country>
) {

    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                dialogTitle = "Select Country Code",
                countryList = countryList,
                selectedCountry,
                openAlertDialog
            )
        }
    }
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    countryList: List<Country>,
    selectedCountry: MutableState<Country>,
    openAlertDialog: MutableState<Boolean>
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle ,color =  MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
        },
        text = {
            DisplayCountryCodes(countryList,selectedCountry,openAlertDialog, onDismissRequest)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {},
        dismissButton = {},
        modifier = Modifier.fillMaxSize(0.8f)
            .background(color =  MaterialTheme.colorScheme.background, shape = RoundedCornerShape(32.dp))
    )
}

@Composable
fun DisplayCountryCodes(
    country: List<Country>,
    selectedCountry: MutableState<Country>,
    openAlertDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit
) {

    var searchCountry by remember { mutableStateOf("") }

    val trimmedSearch = searchCountry.trim()
    val filteredCountries = country.filter {
        it.name.lowercase().contains(trimmedSearch.lowercase()) ||
            it.dialCode.lowercase().contains(trimmedSearch.lowercase())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchCountry,
            onValueChange = { searchCountry = it },
            placeholder = { Text("Enter Country..", maxLines = 1 ,color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp) },
            modifier = Modifier.height(56.dp),
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Check Icon",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(18.dp)
                )
            },
            textStyle = TextStyle(fontSize = 14.sp, lineHeight = 14.sp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredCountries.isEmpty() && trimmedSearch.isNotEmpty()) {
            Text(
                text = "No countries found",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            LazyColumn {
                items(filteredCountries) { country ->
                    DisplayCountryItem(country,selectedCountry,openAlertDialog, onDismissRequest)
                }
            }
        }
    }

}

@Composable
fun DisplayCountryItem(
    country: Country,
    selectedCountry: MutableState<Country>,
    openAlertDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {
        selectedCountry.value = country
        onDismissRequest()
    }){

        Text(text = country.dialCode, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = country.name, color = MaterialTheme.colorScheme.onBackground, overflow = TextOverflow.Ellipsis, maxLines = 1)

    }
}
