package com.example.dicegame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// This composable displays the "About" screen with student info and plagiarism declaration
@Composable
fun AboutScreen(
    onBack: () -> Unit // Lambda function to handle closing the dialog
) {
    AlertDialog(
        onDismissRequest = onBack, // Triggered when the dialog is dismissed
        title = { Text("About") }, // Dialog title

        // Dialog content (student ID, name, and plagiarism declaration)
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Student ID text
                Text(
                    text = "Student ID: w1898945",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp)) // Space between text items

                // Student Name text
                Text(
                    text = "Name: Hasni Haleemdeen",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp)) // More space before declaration

                // Plagiarism declaration text
                Text(
                    text = "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify
                )
            }
        },

        // Confirm button to close the dialog
        confirmButton = {
            Button(onClick = onBack) {
                Text("Close")
            }
        }
    )
}
