package com.example.jatrenamma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    // Coordinate for a sample Jatre location (e.g., Banashankari Temple, Bangalore)
    val jatreCenter = remember { LatLng(12.9253, 77.5737) }
    val parkingPos = remember { LatLng(12.9265, 77.5745) }
    val medicalPos = remember { LatLng(12.9245, 77.5725) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(jatreCenter, 16f)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Jatre Map & Safety", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // Main Temple Marker
                Marker(
                    state = rememberMarkerState(position = jatreCenter),
                    title = "Main Jatre Venue",
                    snippet = "Main Temple Entrance"
                )

                // Parking Marker
                Marker(
                    state = rememberMarkerState(position = parkingPos),
                    title = "Parking Area",
                    snippet = "Authorized Parking"
                )

                // First Aid Marker
                Marker(
                    state = rememberMarkerState(position = medicalPos),
                    title = "First-Aid Center",
                    snippet = "Emergency Medical Help"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Safety Guidelines", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "• Follow the designated path for the Chariot.\n• Emergency First-Aid is near the main entrance.\n• Keep your children close.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
