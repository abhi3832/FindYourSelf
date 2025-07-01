package com.example.findyourself.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.findyourself.dataClasses.CommonInterests
import com.example.findyourself.screens.bottomNavigatoinScreens.ModernInterestSelectionCard
import com.example.findyourself.ui.theme.FindYourselfTheme


private val previewFun : @Composable () -> Unit = {

    val selectedInterests = remember { mutableStateListOf<CommonInterests>() }
    val selectedInterestsString = remember {mutableStateListOf<String>()}

    ModernInterestSelectionCard(
        selectedInterests = selectedInterests,
        selectedInterestsString = selectedInterestsString
    )

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightPreview() {
    FindYourselfTheme(darkTheme = false) {
        previewFun()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
@Composable
fun DarkPreview() {
    FindYourselfTheme(darkTheme = true) {
        previewFun()
    }
}
