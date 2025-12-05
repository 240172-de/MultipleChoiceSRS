package com.example.multiplechoicesrs.model.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.datastore.DataStoreInstance
import kotlinx.coroutines.launch

class SelectNumDialogViewModel(application: Application) : AndroidViewModel(application) {
    var numToStudy by mutableStateOf("")
        private set

    init {
        initFromDataStore()
    }

    fun updateNumToStudy(input: String) {
        if (input.isDigitsOnly()) {
            numToStudy = input
        }
    }

    fun updateDataStore() {
        try {
            saveToDataStore(numToStudy.toInt())
        } catch (_: NumberFormatException) {
            //If empty reset to last saved value
            initFromDataStore()
        }
    }

    private fun initFromDataStore() {
        viewModelScope.launch {
            DataStoreInstance.getIntPreferences(
                getApplication(),
                DataStoreInstance.PreferencesKeys.NUM_TO_STUDY
            ).collect { valueInt ->
                numToStudy = valueInt?.toString() ?: DEFAULT_NUM_TO_STUDY
            }
        }
    }

    private fun saveToDataStore(numToStudy: Int) {
        viewModelScope.launch {
            DataStoreInstance.saveIntPreferences(getApplication(), DataStoreInstance.PreferencesKeys.NUM_TO_STUDY, numToStudy)
        }
    }

    private companion object {
        const val DEFAULT_NUM_TO_STUDY = "5"
    }
}