package com.example.quizapp.ui.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.ui.components.ConfirmDialog
import com.example.quizapp.ui.components.DemoModeNote
import com.example.quizapp.ui.components.ScreenTopBar
import com.example.quizapp.ui.components.SurfaceCard
import com.example.quizapp.ui.components.UserAvatar
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onAboutClick: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    val c = AppTheme.colors
    val context = LocalContext.current
    val session by viewModel.session.collectAsStateWithLifecycle()
    val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()
    var showClearDialog by rememberSaveable { mutableStateOf(false) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var showHelpDialog by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Settings")
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SurfaceCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(session?.name.orEmpty(), size = 52.dp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(session?.name.orEmpty(), color = c.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(session?.email.orEmpty(), color = c.textSecondary, fontSize = 13.sp)
                    }
                }
            }

            SettingsItem(
                icon = Icons.Outlined.DarkMode,
                title = "Dark Mode",
                onClick = { viewModel.setDarkMode(!darkMode) },
                trailing = {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = viewModel::setDarkMode,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Brand.Blue,
                            uncheckedThumbColor = c.textSecondary,
                            uncheckedTrackColor = c.surfaceHigh,
                            uncheckedBorderColor = c.border,
                        ),
                    )
                },
            )
            SettingsItem(Icons.Outlined.DeleteOutline, "Clear Results", onClick = { showClearDialog = true })
            SettingsItem(Icons.Outlined.Info, "About App", onClick = onAboutClick)
            SettingsItem(Icons.AutoMirrored.Outlined.HelpOutline, "Help & Support", onClick = { showHelpDialog = true })
            SettingsItem(
                Icons.AutoMirrored.Outlined.Logout,
                "Log Out",
                tint = Brand.Incorrect,
                onClick = { showLogoutDialog = true },
            )

            DemoModeNote()
        }
    }

    if (showClearDialog) {
        ConfirmDialog(
            title = "Clear all results?",
            message = "This permanently deletes every saved quiz attempt and best score. This can't be undone.",
            confirmText = "Clear",
            destructive = true,
            onConfirm = {
                showClearDialog = false
                viewModel.clearResults {
                    Toast.makeText(context, "Results cleared", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { showClearDialog = false },
        )
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = "Log out?",
            message = "You will need to log in again to continue. Your saved results stay on this device.",
            confirmText = "Log Out",
            destructive = true,
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout(onLoggedOut)
            },
            onDismiss = { showLogoutDialog = false },
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            containerColor = c.surfaceHigh,
            titleContentColor = c.textPrimary,
            textContentColor = c.textSecondary,
            title = { Text("Help & Support", fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    "• Pick a category from Home or Categories to start a 10-question quiz.\n" +
                        "• Choose one answer, then tap Next. Answers can't be changed once chosen.\n" +
                        "• Wrong answers show the correct one so you can learn from it.\n" +
                        "• Every finished quiz is saved in Results with your score and time.\n" +
                        "• Leaving a quiz before the end discards that attempt."
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Got it", color = AppTheme.colors.accent, fontWeight = FontWeight.SemiBold)
                }
            },
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val c = AppTheme.colors
    SurfaceCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = if (trailing != null) 8.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = tint ?: c.textPrimary)
            Spacer(Modifier.width(14.dp))
            Text(title, color = tint ?: c.textPrimary, fontSize = 15.sp, modifier = Modifier.weight(1f))
            if (trailing != null) {
                trailing()
            } else {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = c.textSecondary)
            }
        }
    }
}
