package com.example.findyourself.view.screens.bottomNavigatoinScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.model.User
import com.example.findyourself.view.theme.FindYourselfTheme
import com.example.findyourself.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen() {
    val userViewModel: UserViewModel = koinViewModel()

    val user by userViewModel.user.collectAsState()

    ProfileScreenContent(user)
}

@Composable
private fun ProfileScreenContent(
    user: User?,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
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
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = user.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                textAlign = TextAlign.Center,
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

private class UserPreview: PreviewParameterProvider<User> {
    override val values: Sequence<User>
        get() = sequenceOf(
            User(
                uid = "123456",
                name = "VeryLong NamePerson SurnameSurname",
                phone = "1234567",
                username = "Pippo",
                gender = "Male",
                age = 25,
                isOnline = true,
            ),
            User(
                uid = "123456",
                name = "VeryLong NamePerson SurnameSurname",
                phone = "1234567",
                username = "Pippo",
                gender = "Male",
                age = 25,
                isOnline = false,
            ),
            User(
                uid = "123456",
                name = "VeryLong NamePerson SurnameSurname",
                phone = "1234567",
                username = "Pippo",
                gender = "Male",
                age = 25,
                isOnline = false,
                aboutYourself = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            ),
        )
}

@Preview
@Composable
private fun ProfileScreenPreview(
    @PreviewParameter(UserPreview::class)
    user: User?
) {
    FindYourselfTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ProfileScreenContent(
                user = user
            )
        }
    }
}
