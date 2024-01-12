package com.example.freshtracker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    private val _notificationRequest = MutableLiveData<Pair<String, String>>()
    val notificationRequest: LiveData<Pair<String, String>> get() = _notificationRequest

    fun notifyExpirationApproaching(productName: String) {
        _notificationRequest.value = Pair("Приближается срок годности", "Срок годности продукта '$productName' заканчивается завтра.")
    }
}
