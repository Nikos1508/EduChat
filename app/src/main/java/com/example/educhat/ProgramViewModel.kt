package com.example.educhat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educhat.data.model.Program
import com.example.educhat.data.network.ProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgramViewModel : ViewModel() {
    private val _programs = MutableStateFlow<List<Program>>(emptyList())
    val programs: StateFlow<List<Program>> = _programs

    init {
        loadPrograms()
    }

    private fun loadPrograms() {
        viewModelScope.launch {
            try {
                _programs.value = ProgramRepository.getPrograms()
            } catch (e: Exception) {
                // handle or log error
            }
        }
    }
}