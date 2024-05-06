package com.example.sleeprecorder.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleeprecorder.ui.model.AudioTypes


@Composable
fun AudioTypeDropdown(selectedType: AudioTypes, onTypeSelected: (AudioTypes) -> Unit) {
    val expanded = remember { mutableStateOf(false) }

    Box {
        TextButton(
            onClick = { expanded.value = true },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(selectedType.name)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Icon")
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            AudioTypes.values().forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected(type)
                        expanded.value = false
                    },
                    text= {Text(type.name)}
                )
            }
        }
    }
}

