package com.example.jatrenamma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jatrenamma.model.LostItem
import com.example.jatrenamma.ui.JatreViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LostAndFoundScreen(viewModel: JatreViewModel) {
    val items by viewModel.lostItems.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Lost", "Found")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }, containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            val filteredItems = items.filter { it.type == tabs[selectedTab] }

            if (filteredItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No ${tabs[selectedTab]} items reported yet.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredItems) { item ->
                        LostItemCard(item, onResolve = { resolvedId ->
                            viewModel.resolveItem(resolvedId)
                        })
                    }
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
    var type by remember { mutableStateOf("Lost") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post Lost/Found Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == "Lost", onClick = { type = "Lost" })
                    Text("Lost")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = type == "Found", onClick = { type = "Found" })
                    Text("Found")
                }
                TextField(value = name, onValueChange = { name = it }, label = { Text("Item Name") }, modifier = Modifier.fillMaxWidth())
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                TextField(value = contact, onValueChange = { contact = it }, label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(LostItem(name = name, description = desc, contactNumber = contact, type = type)) },
                enabled = name.isNotBlank() && contact.isNotBlank()
            ) {
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
    val date = remember(item.timestamp) {
        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(item.timestamp))
    }

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
                Text(text = "Posted: $date", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
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
