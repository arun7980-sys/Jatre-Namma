package com.example.jatrenamma.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen() {
    val context = LocalContext.current

    // Initialize osmdroid configuration to avoid requiring network API keys
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Coordinate for a sample Jatre location (e.g., Banashankari Temple, Bangalore)
    val jatreCenter = remember { GeoPoint(12.9253, 77.5737) }
    val parkingPos = remember { GeoPoint(12.9265, 77.5745) }
    val medicalPos = remember { GeoPoint(12.9245, 77.5725) }
    
    // Temporary Stalls
    val sweetStall1 = remember { GeoPoint(12.9255, 77.5730) }
    val sweetStall2 = remember { GeoPoint(12.9251, 77.5732) }
    val toyStall1 = remember { GeoPoint(12.9258, 77.5739) }
    val toyStall2 = remember { GeoPoint(12.9250, 77.5742) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Jatre Map & Safety", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setMultiTouchControls(true)
                        controller.setZoom(17.5)
                        controller.setCenter(jatreCenter)

                        fun addMarker(geoPoint: GeoPoint, title: String, snippet: String) {
                            val marker = Marker(this)
                            marker.position = geoPoint
                            marker.title = title
                            marker.snippet = snippet
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            overlays.add(marker)
                        }

                        // Add Markers
                        addMarker(jatreCenter, "Main Jatre Venue", "Main Temple Entrance")
                        addMarker(parkingPos, "Parking Area", "Authorized Parking")
                        addMarker(medicalPos, "First-Aid Center", "Emergency Medical Help")
                        addMarker(sweetStall1, "Sweet Stall", "Traditional Jilabi & Mysore Pak")
                        addMarker(sweetStall2, "Sweet Food", "Cotton Candy & Snacks")
                        addMarker(toyStall1, "Toy Stall", "Wooden Toys & Balloons")
                        addMarker(toyStall2, "Toy Stall", "Handicrafts & Kids Toys")
                        
                        invalidate() // Refresh map
                    }
                },
                update = { view ->
                    // View updates if state changes
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Safety Guidelines", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "• Follow the designated path for the Chariot.\n• Emergency First-Aid is near the main entrance.\n• Keep your children close.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
