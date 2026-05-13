package com.example.jatrenamma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StoriesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Cultural Stories & Legends", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "The Legend of Jatre",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The village fair, or 'Jatre', is not just a market or a gathering. It is a celebration of our history. Thousands of years ago, it is said that...",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Significance of Rathotsava",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "The pulling of the chariot symbolizes the movement of life and the community coming together to support one another.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
