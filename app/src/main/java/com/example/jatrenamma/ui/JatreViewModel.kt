package com.example.jatrenamma.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jatrenamma.model.Event
import com.example.jatrenamma.model.LostItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class JatreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _lostItems = MutableStateFlow<List<LostItem>>(emptyList())
    val lostItems: StateFlow<List<LostItem>> = _lostItems

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    init {
        fetchEvents()
        fetchLostItems()
    }

    private fun fetchEvents() {
        db.collection("events")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                
                if (snapshot != null && snapshot.isEmpty) {
                    populateMockEventsIfEmpty()
                }
                
                val eventList = snapshot?.toObjects(Event::class.java) ?: emptyList()
                _events.value = eventList
            }
    }

    private fun populateMockEventsIfEmpty() {
        val mockEvents = listOf(
            Event(
                id = UUID.randomUUID().toString(),
                title = "Grand Rathotsava",
                description = "The main chariot pulling ceremony.",
                startTime = "4:00 PM",
                date = "Oct 24",
                location = "Main Temple Street",
                timestamp = System.currentTimeMillis() + 100000,
                isOngoing = true
            ),
            Event(
                id = UUID.randomUUID().toString(),
                title = "Traditional Wrestling",
                description = "Local champions compete in the mud arena.",
                startTime = "6:00 PM",
                date = "Oct 24",
                location = "Village Ground",
                timestamp = System.currentTimeMillis() + 300000,
                isOngoing = false
            ),
            Event(
                id = UUID.randomUUID().toString(),
                title = "Cultural Drama",
                description = "Mythological play performed by local artists.",
                startTime = "8:30 PM",
                date = "Oct 24",
                location = "Open Air Theatre",
                timestamp = System.currentTimeMillis() + 600000,
                isOngoing = false
            ),
            Event(
                id = UUID.randomUUID().toString(),
                title = "Cattle Fair",
                description = "Annual exhibition of finest cattle breeds.",
                startTime = "9:00 AM",
                date = "Oct 25",
                location = "Exhibition Ground",
                timestamp = System.currentTimeMillis() + 86400000,
                isOngoing = false
            )
        )

        mockEvents.forEach { event ->
            db.collection("events").document(event.id).set(event)
        }
    }

    private fun fetchLostItems() {
        db.collection("lost_items")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val itemList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LostItem::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                _lostItems.value = itemList
            }
    }

    fun addLostItem(item: LostItem, imageUri: Uri?) {
        viewModelScope.launch {
            _isUploading.value = true
            try {
                // Bypass Firebase Storage to avoid "Upgrade to Blaze" billing issues.
                // Store the local device URI directly in Firestore instead.
                val finalImageUrl = imageUri?.toString() ?: ""
                
                val newItem = item.copy(imageUrl = finalImageUrl)
                db.collection("lost_items").add(newItem)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isUploading.value = false
            }
        }
    }

    fun resolveItem(itemId: String) {
        db.collection("lost_items").document(itemId).update("isResolved", true)
    }
}
