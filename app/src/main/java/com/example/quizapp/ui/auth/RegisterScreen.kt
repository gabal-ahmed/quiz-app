package com.example.quizapp.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.ui.components.AppLogo
import com.example.quizapp.ui.components.AppTextField
import com.example.quizapp.ui.components.AppTitle
import com.example.quizapp.ui.components.DemoModeNote
import com.example.quizapp.ui.components.PrimaryButton
import com.example.quizapp.ui.components.ScreenTopBar
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit,
) {
    val c = AppTheme.colors
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
            onRegistered()
        }
    }

    val submit = {
        // Hide the keyboard but keep focus, so a hardware Enter key-up can't "click" another control.
        keyboard?.hide()
        viewModel.register(fullName, email, password, confirmPassword)
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        ScreenTopBar(title = "", onBack = onBack)
        Column(
            Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppLogo(size = 84.dp)
            AppTitle(fontSize = 32.sp)
            Spacer(Modifier.height(12.dp))
            Text("Create Account", color = c.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Join Quiz App and start learning!", color = c.textSecondary, fontSize = 14.sp)

            Spacer(Modifier.height(24.dp))
            AppTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Full Name",
                leadingIcon = Icons.Outlined.Person,
                error = state.fullNameError,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
            )
            Spacer(Modifier.height(8.dp))
            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email address",
                leadingIcon = Icons.Outlined.Email,
                error = state.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            )
            Spacer(Modifier.height(8.dp))
            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                error = state.passwordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            )
            Spacer(Modifier.height(8.dp))
            AppTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                error = state.confirmPasswordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
            )

            state.generalError?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Brand.Incorrect, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(20.dp))
            PrimaryButton("Register", onClick = submit, loading = state.isLoading)

            Spacer(Modifier.height(16.dp))
            DemoModeNote()

            Spacer(Modifier.height(24.dp))
            Row {
                Text("Already have an account? ", color = c.textSecondary, fontSize = 14.sp)
                Text(
                    "Login",
                    color = AppTheme.colors.accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onBack),
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
