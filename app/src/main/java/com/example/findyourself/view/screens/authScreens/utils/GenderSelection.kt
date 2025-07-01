package com.example.findyourself.view.screens.authScreens.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun GenderSelection(selectedGender: Gender?, onGenderSelected: (Gender) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GenderOption(label = "Male", isSelected = selectedGender == Gender.Male) {
            onGenderSelected(Gender.Male)
        }
        GenderOption(label = "Female", isSelected = selectedGender == Gender.Female) {
            onGenderSelected(Gender.Female)
        }
        GenderOption(label = "Others", isSelected = selectedGender == Gender.Others) {
            onGenderSelected(Gender.Others)
        }
    }
}

@Composable
fun GenderOption(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable { onClick() }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        RadioButton(
            selected = isSelected,
            colors = RadioButtonColors(
                selectedColor = MaterialTheme.colorScheme.onBackground,
                unselectedColor = Color.Gray,
                disabledSelectedColor = MaterialTheme.colorScheme.onBackground,
                disabledUnselectedColor = MaterialTheme.colorScheme.onBackground,
            ),
            onClick = { onClick() },
            modifier = Modifier.size(24.dp) // Controls radio button size
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Gray)
    }
}

enum class Gender { Male, Female, Others }