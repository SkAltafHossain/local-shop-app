package com.example.localshop.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localshop.core.designsystem.component.AppButton
import com.example.localshop.core.designsystem.component.AppTextField
import com.example.localshop.core.designsystem.component.AppTextButton

@Composable
fun ResetPasswordScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "TODO: Implement reset password with token from email",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Token",
            leadingIcon = Icons.Default.Lock
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppTextField(
            value = "",
            onValueChange = {},
            label = "New Password",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Confirm Password",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        AppButton(
            text = "Reset Password",
            onClick = {}
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppTextButton(
            text = "Back to Login",
            onClick = { navController.popBackStack() }
        )
    }
}