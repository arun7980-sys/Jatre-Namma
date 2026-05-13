package com.example.jatrenamma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jatrenamma.model.LostItem

import androidx.compose.runtime.*
import com.example.jatrenamma.ui.JatreViewModel

@Composable
fun LostAndFoundScreen(viewModel: JatreViewModel) {
    val items by viewModel.lostItems.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(text = "Lost & Found", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    LostItemCard(item, onResolve = { resolvedId ->
                        viewModel.resolveItem(resolvedId)
                    })
                }
            }
        }
        
        if (showDialog) {
            AddItemDialog(
                onDismiss = { showDialog = false },
                onAdd = { newItem ->
                    viewModel.addLostItem(newItem)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddItemDialog(onDismiss: () -> Unit, onAdd: (LostItem) -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post Lost/Found Item") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Item Name") })
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
                TextField(value = contact, onValueChange = { contact = it }, label = { Text("Contact Number") })
            }
        },
        confirmButton = {
            Button(onClick = { onAdd(LostItem(name = name, description = desc, contactNumber = contact)) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun LostItemCard(item: LostItem, onResolve: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                Text(text = item.description, style = MaterialTheme.typography.bodySmall)
                Text(text = "Contact: ${item.contactNumber}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            if (item.isResolved) {
                Icon(Icons.Default.Check, contentDescription = "Resolved", tint = Color.Green)
            } else {
                Button(onClick = { onResolve(item.id) }, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text("Resolve", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
