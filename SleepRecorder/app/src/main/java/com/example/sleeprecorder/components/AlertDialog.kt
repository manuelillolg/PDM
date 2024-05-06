package com.example.sleeprecorder.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.sleeprecorder.R

@Composable
fun PersonalizatedDialog(
    onAcceptClick: () -> Unit,
    title:String,
    text : @Composable ()->Unit,
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title,
                textAlign = TextAlign.Center
            )
        },
        text = { text() },
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = onAcceptClick) {
                Text(text = stringResource(R.string.aceptar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancelar))
            }
        }
    )
}