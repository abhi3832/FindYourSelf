package com.example.findyourself.view.screens.bottomNavigatoinScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.findyourself.view.viewModels.ConnectChatViewModel
import com.example.findyourself.view.viewModels.ConnectViewModel
import com.example.findyourself.view.viewModels.UserViewModel


@Composable
fun HistoryScreen(
    mainNavController: NavHostController,
    rootNavController: NavHostController,
    connectViewModel: ConnectViewModel,
    userViewModel: UserViewModel,
    connectChatViewModel: ConnectChatViewModel
) {
    val user = userViewModel.user.value!!

    // 🔁 Start observing user's active chats only once
//    LaunchedEffect(Unit) {
//        connectChatViewModel.startObservingActiveChat(user.uid)
//    }

    Column(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
        //CurrentChatSection(rootNavController, connectViewModel,connectChatViewModel)
        Spacer(modifier = Modifier.padding(top = 8.dp))
        HorizontalDivider()
        StatisticsSection()
        Spacer(modifier = Modifier.padding(top = 8.dp))
        HorizontalDivider()
            //RecentChatsSection(rootNavController)
    }
}

@Composable
fun CurrentChatSection(rootNavController: NavHostController, connectViewModel: ConnectViewModel, connectChatViewModel: ConnectChatViewModel) {

    val activeChat by connectChatViewModel.activeChat.collectAsState()



    Column(modifier = Modifier.fillMaxWidth(),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center)
   {
       if(activeChat == null){
            Text("No Active Chats !", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
       }
       else{

           val chat = activeChat!!

           Log.d("Hello123","Hi there !")

            Row(
                modifier = Modifier
                    .fillMaxWidth().clickable {
//                   rootNavController.navigate(ChatDetailScreens.DetailScreen.createRoute(
//                       chatId = "123",
//                       isChatEnded = false,
//                       isHistoryChat = false
//                   )){launchSingleTop = true}
                    }
                    .padding(16.dp).clip(shape = RoundedCornerShape(16.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f), // Ensures this part takes available space first
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                       // Text(text = chat.participantName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        Text(text = "Currently chatting", fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.alpha(0.7f))
                    }
                }

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(){
                Text("End Chat", color = MaterialTheme.colorScheme.onBackground)

            }
        }
   }
}
@Composable
fun StatisticsSection() {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly) {
        StatisticItem(label = "Avg. Duration", value = "12m 30s",)
        StatisticItem(label = "Max Duration", value = "45m 12s")
        StatisticItem(label = "Total Chats", value = "124")
    }
}

@Composable
fun StatisticItem(label: String, value: String) {
    Column(modifier = Modifier.padding(end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = label, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
        Text(text = value, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp)
    }
}

@Composable
fun RecentChatsSection(rootNavController: NavHostController) {
    Column(modifier = Modifier) {
        RecentChatItem(name = "Anonymous_789", duration = "23m 45s", time = "Today, 2:30 PM", rootNavController)
        RecentChatItem(
            name = "Anonymous_456",
            duration = "15m 20s",
            time = "Today, 1:15 PM",
            rootNavController = rootNavController
        )
        RecentChatItem(
            name = "Anonymous_234",
            duration = "32m 10s",
            time = "Yesterday",
            rootNavController = rootNavController
        )
    }
}

@Composable
fun RecentChatItem(name: String, duration: String, time: String, rootNavController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth().clickable {
//                    rootNavController.navigate(ChatDetailScreens.DetailScreen.createRoute(
//                        chatId = "123",
//                        isChatEnded = true,
//                        isHistoryChat = true
//                    )){launchSingleTop = true}
                }
                .padding(16.dp).clip(shape = RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f), // Ensures this part takes available space first
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = duration, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.alpha(0.7f))
                }
            }

            Text(time,color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alpha(0.7f), fontSize = 12.sp)
        }


    }

}

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun LightPreview() {
//    FindYourselfTheme(darkTheme = false) {
//        HistoryScreen()
//    }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FindYourselfTheme(darkTheme = true) {
//        HistoryScreen()
//    }
//}
//
