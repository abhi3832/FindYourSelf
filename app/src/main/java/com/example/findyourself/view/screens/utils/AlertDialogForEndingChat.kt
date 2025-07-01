package com.example.findyourself.view.screens.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.findyourself.view.viewModels.ConnectChatViewModel
import com.example.findyourself.view.viewModels.ConnectViewModel

@Composable
fun AlertDialogForEndingChat(
    openAlertDialog: MutableState<Boolean>,
    rootNavController: NavHostController,
    chatId: String,
    myUserId: String,
    participantId: String,
    connectChatViewModel: ConnectChatViewModel,
    connectViewModel: ConnectViewModel,
    whoEndedChat: MutableState<Boolean>
) {
    // ...


    // ...
    when {
        // ...
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    whoEndedChat.value = true
                    openAlertDialog.value = false
                    connectChatViewModel.endActiveChat()

                },
                dialogTitle = "End Chat",
                dialogText = " Are you sure, you want to End chat ?",
                icon = Icons.Default.Info
            )
        }
    }
}


@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon", tint =  MaterialTheme.colorScheme.onBackground)
        },
        title = {
            Text(text = dialogTitle ,color =  MaterialTheme.colorScheme.onBackground)
        },
        text = {
            Column(){
                Text(text = dialogText,color =  MaterialTheme.colorScheme.onBackground)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Yes",color =  MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel",color =  MaterialTheme.colorScheme.onBackground )
            }
        },
        //modifier = Modifier.background(color =  MaterialTheme.colorScheme.background)
    )
}