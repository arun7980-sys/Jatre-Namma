package com.example.jatrenamma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Jatre Map & Safety", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Custom Jatre Map Placeholder\n(Markers: Parking, First-Aid, Stalls)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Safety Guidelines", style = MaterialTheme.typography.titleMedium)
        Text(text = "• Follow the designated path for the Chariot.\n• Emergency First-Aid is near the main entrance.\n• Keep your children close.", style = MaterialTheme.typography.bodyMedium)
    }
}
