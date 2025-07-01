package com.example.findyourself.view.screens.bottomNavigatoinScreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.findyourself.model.CommonInterests
import com.example.findyourself.model.ConnectResponse
import com.example.findyourself.model.User
import com.example.findyourself.view.navigation.ConnectChatDetailScreen
import com.example.findyourself.model.ActiveChat
import com.example.findyourself.view.screens.authScreens.showToast
import com.example.findyourself.view.viewModels.ConnectChatViewModel
import com.example.findyourself.view.viewModels.ConnectUiState
import com.example.findyourself.view.viewModels.ConnectViewModel
import com.example.findyourself.view.viewModels.CreateConnectChatUiState
import com.example.findyourself.view.viewModels.LoadChatUserUiState
import com.example.findyourself.view.viewModels.UserViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
fun ConnectScreen(
    mainNavController: NavHostController,
    rootNavController: NavHostController,
    connectViewModel: ConnectViewModel,
    userViewModel: UserViewModel,
    connectChatViewModel: ConnectChatViewModel
) {


    val myUser = userViewModel.user.value
    val activeChat by connectChatViewModel.activeChat.collectAsState()
    val matchedUser = connectViewModel.participant.collectAsState()
    val chatId = remember { mutableStateOf("") }

    LaunchedEffect(chatId.value) {
        if(chatId.value.isNotEmpty()) connectChatViewModel.observeActiveChat(chatId.value, myUser?.uid ?: "")
    }

    Log.d("ActiveChat", "ActiveChat Value from Connect Screen :  $activeChat")

    var genderPreference by remember { mutableStateOf("Both") }
    val selectedInterests = remember { mutableStateListOf<CommonInterests>() }
    val selectedInterestsString = remember { mutableStateListOf<String>() }
    val context = LocalContext.current



    val showLoadingIndicatorForConnecting = remember { mutableStateOf(false) }
    val showLoadingIndicatorForGettingUser = remember { mutableStateOf(false) }
    val showLoadingIndicatorForCreatingChat = remember { mutableStateOf(false) }

    val userConnected = remember { mutableStateOf(false) }
    val userLoaded = remember { mutableStateOf(false) }
    val userFirebaseChatCreated = remember { mutableStateOf(false) }

    val userConnectedResponse = remember { mutableStateOf<ConnectResponse?>(null) }


    LaunchedEffect(Unit) {

        connectViewModel.connectState.collect { response ->
            when (response) {
                is ConnectUiState.Idle -> {
                    chatId.value = ""
                    userConnected.value = false
                    showLoadingIndicatorForConnecting.value = false
                }

                is ConnectUiState.Loading -> {
                    chatId.value = ""
                    userConnected.value = false
                    showLoadingIndicatorForConnecting.value = true
                }

                is ConnectUiState.Success -> {
                    showLoadingIndicatorForConnecting.value = false
                    userConnected.value = true
                    val data = response.response

                    userConnectedResponse.value = data

                    if (data.isConnected && data.chatId != null && data.matchedUser != null) {
                        chatId.value = data.chatId
                        connectViewModel.loadChatUser(data.matchedUser)
                    }
                }

                is ConnectUiState.Failure -> {
                    chatId.value = ""
                    showLoadingIndicatorForConnecting.value = false
                    userConnected.value = false
                    showToast(context, "Couldn't Connect to a user")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        connectViewModel.chatUser.collect { chatUser ->
            when (chatUser) {
                is LoadChatUserUiState.Idle -> {
                    chatId.value = ""
                    showLoadingIndicatorForGettingUser.value = false
                    userLoaded.value = false
                }

                is LoadChatUserUiState.Loading -> {
                    chatId.value = ""
                    showLoadingIndicatorForGettingUser.value = true
                    userLoaded.value = false
                }

                is LoadChatUserUiState.Success -> {
                    showLoadingIndicatorForGettingUser.value = false

                    if (userConnectedResponse.value != null) {
                        userLoaded.value = true
                        connectViewModel.createConnectChat(
                            userConnectedResponse.value!!.chatId!!,
                            userViewModel.user.value!!, chatUser.user
                        )
                    }
                }

                is LoadChatUserUiState.Failure -> {
                    chatId.value = ""
                    userLoaded.value = false
                    showLoadingIndicatorForGettingUser.value = false
                    showToast(context, "Couldn't Fetch User !")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        connectViewModel.createConnectChatUiState.collect { response ->
            when (response) {
                is CreateConnectChatUiState.Idle -> {
                    chatId.value = ""
                    showLoadingIndicatorForCreatingChat.value = false
                    userFirebaseChatCreated.value = false
                }

                is CreateConnectChatUiState.Loading -> {
                    chatId.value = ""
                    showLoadingIndicatorForCreatingChat.value = true
                    userFirebaseChatCreated.value = false
                }

                is CreateConnectChatUiState.Success -> {
                    showLoadingIndicatorForCreatingChat.value = false
                    userFirebaseChatCreated.value = true
                }

                is CreateConnectChatUiState.Failure -> {
                    chatId.value = ""
                    userFirebaseChatCreated.value = false
                    showLoadingIndicatorForCreatingChat.value = false
                    showToast(context, "Couldn't Create Firebase chat!")
                }
            }
        }
    }

    LaunchedEffect(userConnected.value, userLoaded.value, userFirebaseChatCreated.value) {
        if (userConnected.value && userLoaded.value && userFirebaseChatCreated.value && userConnectedResponse.value != null) {
            rootNavController.navigate(
                ConnectChatDetailScreen.DetailScreen.createRoute(
                    chatId = userConnectedResponse.value!!.chatId!!
                )
            ) { launchSingleTop = true }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GreetingCard(
            userName = myUser?.name ?: "",
            onlineCount = 2223
        )


        ActiveChatCard(
            activeChat = activeChat,
            matchedUser = matchedUser.value,
            hasNotification = true,
            modifier = Modifier,
            rootNavController
        )


        // Preferences Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val cardColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = BorderStroke(
                        width = 0.8.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Your Preferences",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        Text(
                            text = "I want to chat with:",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val options = listOf("Male" to Icons.Default.Male, "Female" to Icons.Default.Female, "Both" to Icons.Default.People)

                            options.forEach { (label, icon) ->
                                FilterChip(
                                    selected = genderPreference == label,
                                    onClick = { genderPreference = label },
                                    label = {
                                        Text(
                                            label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (genderPreference == label)
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            tint = if (genderPreference == label)
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                ModernInterestSelectionCard(selectedInterests,selectedInterestsString)

                SafetyMessage()

            Button(
                onClick = {
                    if (userViewModel.user.value != null) {
                        connectViewModel.connect(userViewModel.user.value!!.uid, selectedInterestsString)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                //.clip(RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                //elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (!showLoadingIndicatorForConnecting.value && !showLoadingIndicatorForGettingUser.value && !showLoadingIndicatorForCreatingChat.value) {
                    Text(
                        text = "Start Random Chat",
                        color = MaterialTheme.colorScheme.background
                    )
                } else if (showLoadingIndicatorForConnecting.value && !showLoadingIndicatorForGettingUser.value && !showLoadingIndicatorForCreatingChat.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = "Matching...",
                            color = MaterialTheme.colorScheme.background
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.Red
                        )
                    }
                } else if (!showLoadingIndicatorForConnecting.value && showLoadingIndicatorForGettingUser.value && !showLoadingIndicatorForCreatingChat.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = "Fetching User details...",
                            color = MaterialTheme.colorScheme.background
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.Red
                        )
                    }
                } else if (!showLoadingIndicatorForConnecting.value && !showLoadingIndicatorForGettingUser.value && showLoadingIndicatorForCreatingChat.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = "Creating Chat...",
                            color = MaterialTheme.colorScheme.background
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveChatCard(
    activeChat: ActiveChat?,
    matchedUser: User?,
    hasNotification: Boolean,
    modifier: Modifier = Modifier,
    rootNavController: NavHostController
) {

    val cardColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    Card(
        modifier = modifier
            .fillMaxWidth().clickable {
                if(activeChat != null && activeChat.chatId.isNotEmpty()){
                    rootNavController.navigate(
                        ConnectChatDetailScreen.DetailScreen.createRoute(
                            chatId = activeChat.chatId
                        )
                    ) { launchSingleTop = true }
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        if (activeChat != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "You have an active chat 💬",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Chatting with ${matchedUser?.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (hasNotification) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "New Messages",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Active Chat",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Start a conversation and meet someone new!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun SafetyMessage(){

    val cardColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Stay safe! Don't share personal information with strangers.",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModernInterestSelectionCard(
    selectedInterests: SnapshotStateList<CommonInterests>,
    selectedInterestsString: SnapshotStateList<String>
) {
    var isExpanded by remember { mutableStateOf(false) }
    val cardColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Header Section with gradient-style title
            Text(
                text = "Your Interests",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Selected Interests ---
            Text(
                text = "Selected",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedInterests.isEmpty()) {
                Text(
                    text = "No interests selected yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    selectedInterests.forEach { interest ->
                        AssistChip(
                            onClick = {},
                            label = { Text(interest.interest) },
                            leadingIcon = {
                                Icon(imageVector = interest.id, contentDescription = null)
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Expandable Section: All Interests ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = if (isExpanded) "Hide All Interests" else "Show All Interests",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CommonInterests.entries.sortedBy { it.interest.length }.forEach { interest ->
                        val isSelected = selectedInterests.contains(interest)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedInterests.remove(interest)
                                    selectedInterestsString.remove(interest.name)
                                } else {
                                    selectedInterests.add(interest)
                                    selectedInterestsString.add(interest.name)
                                }
                            },
                            label = {
                                Text(
                                    interest.interest,
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = interest.id,
                                    contentDescription = null,
                                    tint = if (isSelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun GreetingCard(
    userName: String,
    onlineCount: Int,
    modifier: Modifier = Modifier
) {
    val cardColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Hello, $userName 👋",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Meet someone new in seconds!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$onlineCount people online",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



