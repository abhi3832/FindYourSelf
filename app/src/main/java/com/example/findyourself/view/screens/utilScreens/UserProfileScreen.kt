package com.example.findyourself.view.screens.utilScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.view.screens.bottomNavigatoinScreens.StatisticsSection
import com.example.findyourself.view.viewModels.ConnectViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserProfileScreenForRandomChat(userId: String) {
    val connectViewModel: ConnectViewModel = koinViewModel()

    val showLoadingIndicatorForGettingUser = remember { mutableStateOf(false) }
    val user = connectViewModel.participant.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize().background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBarForUserProfile()
        if(user.value != null){

            val chatUser = user.value!!

            Box(
                modifier = Modifier.shadow(40.dp, CircleShape, spotColor = MaterialTheme.colorScheme.onBackground)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray) ,
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Picture", tint = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Name and Username
            Text(
                text = chatUser.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = chatUser.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )

            StatisticsSection()

            // Personal Info Section
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileInfoRow("Gender", chatUser.gender, Icons.Default.Person)
                ProfileInfoRow("Age", (chatUser.age).toString(), Icons.Default.DateRange)
                ProfileInfoRow("About Yourself", chatUser.aboutYourself ?: "", Icons.Default.Message)
            }

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String, icon: ImageVector) {

    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TopBarForUserProfile() {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Ensuring one part aligns left, the other right
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
    }
}
