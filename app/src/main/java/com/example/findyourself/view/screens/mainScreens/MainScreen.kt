package com.example.findyourself.view.screens.mainScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.findyourself.view.navigation.BottomNavigation
import com.example.findyourself.view.navigation.MainNavGraph
import com.example.findyourself.view.viewModels.AuthViewModel
import com.example.findyourself.view.viewModels.ConnectChatViewModel
import com.example.findyourself.view.viewModels.ConnectViewModel
import com.example.findyourself.view.viewModels.MessageViewModel
import com.example.findyourself.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    rootNavController: NavHostController,
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()
    val connectViewModel: ConnectViewModel = koinViewModel()
    val connectChatViewModel: ConnectChatViewModel = koinViewModel()
    val messageViewModel: MessageViewModel = koinViewModel()

    val homeNavController = rememberNavController()

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background).navigationBarsPadding(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth().height(70.dp).background(color = MaterialTheme.colorScheme.background),
                backgroundColor = MaterialTheme.colorScheme.background,
                elevation = 10.dp
            ) {
                BottomNavigationBar(homeNavController, authViewModel, userViewModel, connectChatViewModel)
            }
        },
        topBar = {TopAppBarWithIcons(homeNavController)}
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            MainNavGraph(homeNavController, rootNavController, authViewModel, userViewModel,connectViewModel,connectChatViewModel, messageViewModel)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithIcons(homeNavController: NavHostController) {

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("CurrentRoute", "CurrentRoute : $currentRoute")


    val screenTitles = mapOf(
        "chats" to "Chats",
        "connect" to "Connect",
        "profile" to "Profile",
        "notification" to "Inbox",
        "history" to "History"
    )

    Column(modifier = Modifier.fillMaxWidth()){
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = screenTitles[currentRoute] ?: "Chats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                actionIconContentColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
        )

        if(screenTitles[currentRoute] == "Chats"){
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp).height(50.dp)){
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null)},
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f).clip(RoundedCornerShape(40.dp)),
                    shape = RoundedCornerShape(40.dp),
                    placeholder = { Text("Search Chats...") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
            }


        }
    }


}


@Composable
fun BottomNavigationBar(
    homeNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    connectChatViewModel: ConnectChatViewModel
) {

    val items = BottomNavigation.entries
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.background)) {
        items.forEach { screen ->

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.screenIcon,
                        contentDescription = screen.screenName,
                        modifier = Modifier.size( 30.dp) // Hide the middle icon since FAB replaces it
                    )
                },
                label = { Text(screen.screenName) },
                selected = currentRoute == screen.screenRoute,
                onClick = { if (currentRoute != screen.screenRoute) {
                    Log.d("Jaipur", "CurrentRoute : $currentRoute")
                    Log.d("Jaipur", "ScreenRoute : ${screen.screenRoute}")
                    homeNavController.navigate(screen.screenRoute) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }},
                alwaysShowLabel = false
            )
        }
    }
}


