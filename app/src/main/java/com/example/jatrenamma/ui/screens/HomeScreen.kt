package com.example.jatrenamma.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.jatrenamma.model.Event
import com.example.jatrenamma.ui.JatreViewModel
import com.example.jatrenamma.worker.NotificationWorker
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(viewModel: JatreViewModel) {
    val events by viewModel.events.collectAsState()
    val context = LocalContext.current
    
    // Permission launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notifications permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Extract unique dates from events
    val dates = remember(events) {
        events.map { it.date }.distinct().sorted()
    }
    
    var selectedDate by remember(dates) {
        mutableStateOf(dates.firstOrNull() ?: "")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Live Schedule",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (dates.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(dates) { date ->
                        DateChip(
                            date = date,
                            isSelected = selectedDate == date,
                            onClick = { selectedDate = date }
                        )
                    }
                }
            }
        }

        val filteredEvents = remember(selectedDate, events) {
            if (selectedDate.isEmpty()) events else events.filter { it.date == selectedDate }
        }

        if (filteredEvents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No events scheduled for this day.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredEvents) { event ->
                    EventCard(event)
                }
            }
        }
    }
}

@Composable
fun DateChip(date: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = date,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EventCard(event: Event) {
    val context = LocalContext.current
    var isReminded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isOngoing) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title, 
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = {
                    scheduleNotification(context, event)
                    isReminded = true
                    Toast.makeText(context, "Reminder set for ${event.title}", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = if (isReminded) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                        contentDescription = "Set Reminder",
                        tint = if (isReminded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (event.isOngoing) {
                    Surface(
                        color = Color.Red,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.startTime, 
                style = MaterialTheme.typography.bodyMedium, 
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(text = "📍 ${event.location}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun scheduleNotification(context: android.content.Context, event: Event) {
    val workManager = WorkManager.getInstance(context)
    
    // For demo purposes, we schedule it 5 seconds from now. 
    // In a real app, you would calculate (event.timestamp - currentTime)
    val delay = 5L 

    val data = workDataOf(
        "title" to "Upcoming: ${event.title}",
        "message" to "Starting at ${event.startTime} at ${event.location}"
    )

    val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.SECONDS)
        .setInputData(data)
        .build()

    workManager.enqueue(notificationWork)
}
