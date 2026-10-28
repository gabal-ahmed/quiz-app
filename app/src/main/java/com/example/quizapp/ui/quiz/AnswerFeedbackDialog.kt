package com.example.quizapp.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.ui.components.PrimaryButton
import com.example.quizapp.ui.theme.Brand

/** "Oops!" card shown over the question after a wrong answer (Wrong Answer Feedback screen). */
@Composable
fun AnswerFeedbackDialog(
    correctLetter: String,
    correctAnswer: String,
    isLastQuestion: Boolean,
    onNext: () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Brand.Incorrect),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text("Oops!", color = Color(0xFF0B1630), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("That's not the correct answer.", color = Color(0xFF5B6785), fontSize = 14.sp)

            Spacer(Modifier.height(16.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8EEFF))
                    .padding(14.dp),
            ) {
                Text("Correct answer:", color = Color(0xFF0B1630), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brand.Correct),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(correctLetter, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(correctAnswer, color = Color(0xFF0B1630), fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(20.dp))
            PrimaryButton(if (isLastQuestion) "See Results" else "Next", onClick = onNext)
        }
    }
}
