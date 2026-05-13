package com.example.jatrenamma.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jatrenamma.model.Event
import com.example.jatrenamma.model.LostItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JatreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _lostItems = MutableStateFlow<List<LostItem>>(emptyList())
    val lostItems: StateFlow<List<LostItem>> = _lostItems

    init {
        fetchEvents()
        fetchLostItems()
    }

    private fun fetchEvents() {
        db.collection("events").addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener
            val eventList = snapshot?.toObjects(Event::class.java) ?: emptyList()
            _events.value = eventList
        }
    }

    private fun fetchLostItems() {
        db.collection("lost_items").addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener
            val itemList = snapshot?.toObjects(LostItem::class.java) ?: emptyList()
            _lostItems.value = itemList
        }
    }

    fun addLostItem(item: LostItem) {
        db.collection("lost_items").add(item)
    }

    fun resolveItem(itemId: String) {
        db.collection("lost_items").document(itemId).update("resolved", true)
    }
}
