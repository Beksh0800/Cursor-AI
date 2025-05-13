package com.beka.jaqsyadet.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beka.jaqsyadet.data.model.Habit
import com.beka.jaqsyadet.data.model.HabitCheck
import com.beka.jaqsyadet.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    private val repository: HabitRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val habitId: Long = checkNotNull(savedStateHandle["habitId"])

    private val _uiState = MutableStateFlow<HabitDetailUiState>(HabitDetailUiState.Loading)
    val uiState: StateFlow<HabitDetailUiState> = _uiState

    private val _checks = MutableStateFlow<List<HabitCheck>>(emptyList())
    val checks: StateFlow<List<HabitCheck>> = _checks

    init {
        loadHabitDetail()
        loadHabitChecks()
    }

    private fun loadHabitDetail() {
        viewModelScope.launch {
            try {
                val habitResult = repository.getHabitById(habitId)
                val habit = habitResult.getOrNull()
                if (habit != null) {
                    _uiState.value = HabitDetailUiState.Success(habit)
                } else {
                    _uiState.value = HabitDetailUiState.Error("Habit not found")
                }
            } catch (e: Exception) {
                _uiState.value = HabitDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadHabitChecks() {
        viewModelScope.launch {
            repository.getHabitChecks(habitId)
                .catch { error ->
                    // Handle error
                }
                .collect { checks ->
                    _checks.value = checks
                }
        }
    }

    fun markAsCompleted() {
        viewModelScope.launch {
            try {
                repository.markHabitAsCompleted(habitId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class HabitDetailUiState {
    data object Loading : HabitDetailUiState()
    data class Success(val habit: Habit) : HabitDetailUiState()
    data class Error(val message: String) : HabitDetailUiState()
} 