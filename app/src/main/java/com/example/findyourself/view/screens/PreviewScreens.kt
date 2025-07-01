package com.example.findyourself.view.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.findyourself.model.CommonInterests
import com.example.findyourself.view.screens.bottomNavigatoinScreens.ModernInterestSelectionCard
import com.example.findyourself.view.theme.FindYourselfTheme


private val previewFun : @Composable () -> Unit = {

    val selectedInterests = remember { mutableStateListOf<CommonInterests>() }
    val selectedInterestsString = remember {mutableStateListOf<String>()}

    ModernInterestSelectionCard(
        selectedInterests = selectedInterests,
        selectedInterestsString = selectedInterestsString
    )

}

class MyPreviewParameter: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightPreview(
    @PreviewParameter(MyPreviewParameter::class)
    isDarkTheme: Boolean,
) {
    FindYourselfTheme(darkTheme = isDarkTheme) {
        previewFun()
    }
}