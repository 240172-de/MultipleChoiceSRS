package com.example.multiplechoicesrs.model.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.datastore.DataStoreInstance
import com.example.multiplechoicesrs.model.ConnectionData
import kotlinx.coroutines.launch

class ImportSettingsDialogViewModel(application: Application): AndroidViewModel(application) {
    var ipAddress by mutableStateOf("")
    var port by mutableStateOf("")

    init {
        initFromDataStore()
    }

    fun updateDataStore() {
        saveToDataStore(
            ConnectionData(ipAddress, port)
        )
    }

    fun resetData() {
        initFromDataStore()
    }

    private fun initFromDataStore() {
        viewModelScope.launch {
            DataStoreInstance.getConnectionPreferences(getApplication()).collect { data ->
                ipAddress = data.ipAddress
                port = data.port
            }
        }
    }

    private fun saveToDataStore(data: ConnectionData) {
        viewModelScope.launch {
            DataStoreInstance.saveConnectionPreferences(getApplication(), data)
        }
    }
}