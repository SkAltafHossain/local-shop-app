package com.example.localshop.feature.profile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.AppButton
import com.example.localshop.core.designsystem.component.AppOutlinedButton
import com.example.localshop.core.designsystem.theme.AppTheme
import com.example.localshop.core.navigation.Screen
import com.example.localshop.feature.profile.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val colors = AppTheme.colors
    
    if (!isLoggedIn) {
        NotLoggedInProfileScreen(navController, colors)
    } else {
        LoggedInProfileScreen(navController, colors)
    }
}

@Composable
private fun NotLoggedInProfileScreen(
    navController: NavController,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(120.dp),
            tint = colors.secondaryText.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Welcome to LocalShop",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Sign in to access your profile, orders, wishlist, and more",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AppButton(
            text = "Login",
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppOutlinedButton(
            text = "Create Account",
            onClick = { navController.navigate(Screen.Register.route) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "By continuing, you agree to our Terms of Service and Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = colors.secondaryText.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoggedInProfileScreen(
    navController: NavController,
    colors: com.example.localshop.core.designsystem.theme.AppColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.pageBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Profile Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.primaryText
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "TODO: Implement logged in profile functionality",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText
        )
    }
}