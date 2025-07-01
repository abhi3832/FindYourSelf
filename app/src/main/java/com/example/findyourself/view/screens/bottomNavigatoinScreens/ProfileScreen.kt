package com.example.findyourself.view.screens.bottomNavigatoinScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.navigation.NavHostController
import com.example.findyourself.view.viewModels.AuthViewModel
import com.example.findyourself.view.viewModels.UserViewModel

@Composable
fun ProfileScreen(
    mainNavController: NavHostController,
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    val user = userViewModel.user.value
    Column(
        modifier = Modifier
            .fillMaxSize().background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(user != null) {
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
                text = user.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = user.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Personal Info Section
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileInfoRow("Phone Number", user.phone, Icons.Default.Phone)
                ProfileInfoRow("Gender", user.gender, Icons.Default.Person)
                ProfileInfoRow("About Yourself", user.aboutYourself ?: "", Icons.Default.Message)
                ProfileInfoRow("Age", user.age.toString(), Icons.Default.Person)
            }

            Spacer(modifier = Modifier.height(32.dp))

        }else{
            Text(text = "Error Loading Profile. Please try again later...",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)
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
