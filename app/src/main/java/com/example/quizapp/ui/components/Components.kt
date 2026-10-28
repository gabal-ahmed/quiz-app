package com.example.quizapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.categoryOrNull
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// region Category styling

val Category.icon: ImageVector
    get() = when (this) {
        Category.PROGRAMMING -> Icons.Filled.Code
        Category.NETWORKS -> Icons.Outlined.Hub
        Category.CS_BASICS -> Icons.Outlined.DesktopWindows
    }

val Category.gradient: List<Color>
    get() = when (this) {
        Category.PROGRAMMING -> listOf(Color(0xFF4338F5), Color(0xFF5B6CFF))
        Category.NETWORKS -> listOf(Color(0xFF0E9F6E), Color(0xFF10B981))
        Category.CS_BASICS -> listOf(Color(0xFF7C3AED), Color(0xFF9F5BFF))
    }

@Composable
fun CategoryBadge(category: Category, modifier: Modifier = Modifier, size: Dp = 44.dp) {
    Box(
        modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(Brush.linearGradient(category.gradient)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(category.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun CategoryCard(
    category: Category,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    footer: String? = null,
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.horizontalGradient(category.gradient))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(category.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(category.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp, lineHeight = 16.sp)
            if (footer != null) {
                Spacer(Modifier.height(4.dp))
                Text(footer, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
    }
}

// endregion

// region Layout

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val c = AppTheme.colors
    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(c.backgroundTop, c.backgroundBottom)))
            .drawBehind {
                val glow = if (c.isDark) 0.22f else 0.10f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Brand.Blue.copy(alpha = glow), Color.Transparent),
                        center = Offset(size.width, 0f),
                        radius = size.width,
                    ),
                    radius = size.width,
                    center = Offset(size.width, 0f),
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Brand.Purple.copy(alpha = glow * 0.8f), Color.Transparent),
                        center = Offset(0f, size.height),
                        radius = size.width,
                    ),
                    radius = size.width,
                    center = Offset(0f, size.height),
                )
            },
        content = content,
    )
}

@Composable
fun ScreenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val c = AppTheme.colors
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = c.textPrimary)
            }
        } else {
            Spacer(Modifier.width(12.dp))
        }
        if (leading != null) {
            leading()
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(title, color = c.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            if (subtitle != null) {
                Text(subtitle, color = c.textSecondary, fontSize = 12.sp)
            }
        }
        actions()
    }
}

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = AppTheme.colors
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier
            .clip(shape)
            .background(c.surface)
            .border(1.dp, c.border, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        content = content,
    )
}

// endregion

// region Branding

@Composable
fun AppLogo(modifier: Modifier = Modifier, size: Dp = 96.dp) {
    Image(painterResource(R.drawable.ic_logo), contentDescription = "Quiz App logo", modifier = modifier.size(size))
}

@Composable
fun AppTitle(modifier: Modifier = Modifier, fontSize: TextUnit = 32.sp) {
    val c = AppTheme.colors
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(color = c.textPrimary)) { append("Quiz ") }
            withStyle(SpanStyle(color = AppTheme.colors.accent)) { append("App") }
        },
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )
}

// endregion

// region Buttons & inputs

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Brand.Blue,
            contentColor = Color.White,
            disabledContainerColor = Brand.Blue.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.6f),
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val c = AppTheme.colors
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, Brand.Purple),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = c.textPrimary),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val c = AppTheme.colors
    var visible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (visible) "Hide password" else "Show password",
                    )
                }
            }
        } else {
            null
        },
        visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = c.surface,
            unfocusedContainerColor = c.surface,
            errorContainerColor = c.surface,
            focusedBorderColor = Brand.Blue,
            unfocusedBorderColor = c.border,
            errorBorderColor = Brand.Incorrect,
            focusedTextColor = c.textPrimary,
            unfocusedTextColor = c.textPrimary,
            errorTextColor = c.textPrimary,
            focusedPlaceholderColor = c.textSecondary,
            unfocusedPlaceholderColor = c.textSecondary,
            errorPlaceholderColor = c.textSecondary,
            focusedLeadingIconColor = c.textPrimary,
            unfocusedLeadingIconColor = c.textSecondary,
            errorLeadingIconColor = Brand.Incorrect,
            focusedTrailingIconColor = c.textSecondary,
            unfocusedTrailingIconColor = c.textSecondary,
            errorTrailingIconColor = c.textSecondary,
            errorSupportingTextColor = Brand.Incorrect,
            cursorColor = Brand.Blue,
        ),
    )
}

// endregion

// region Small shared pieces

@Composable
fun UserAvatar(name: String, modifier: Modifier = Modifier, size: Dp = 44.dp) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(Brand.Blue, Brand.Purple))),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            name.trim().firstOrNull()?.uppercase() ?: "?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.42f).sp,
        )
    }
}

@Composable
fun BestScoreCard(
    best: QuizAttempt?,
    modifier: Modifier = Modifier,
    title: String = "Your Best Score",
    onClick: (() -> Unit)? = null,
) {
    val c = AppTheme.colors
    SurfaceCard(modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Brand.Gold, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = c.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(
                    best?.categoryOrNull()?.title ?: "No results yet — start a quiz!",
                    color = c.textSecondary,
                    fontSize = 12.sp,
                )
            }
            if (best != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "${best.correctAnswers}/${best.totalQuestions}",
                        color = Brand.Gold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(formatDate(best.completedAt), color = c.textSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun DemoModeNote(modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Brand.Blue.copy(alpha = 0.12f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(Icons.Outlined.Info, contentDescription = null, tint = AppTheme.colors.accent, modifier = Modifier.size(18.dp))
        Text(
            "Demo Mode: accounts are saved on this device only. Passwords are not stored or verified.",
            color = c.textSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        )
    }
}

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    destructive: Boolean = false,
) {
    val c = AppTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = c.surfaceHigh,
        titleContentColor = c.textPrimary,
        textContentColor = c.textSecondary,
        title = { Text(title, fontWeight = FontWeight.SemiBold) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText, color = if (destructive) Brand.Incorrect else AppTheme.colors.accent, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = c.textSecondary) }
        },
    )
}

fun formatDate(millis: Long): String =
    SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(millis))

fun formatDuration(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}

// endregion
