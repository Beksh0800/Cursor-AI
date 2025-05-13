package com.beka.jaqsyadet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beka.jaqsyadet.data.model.Habit
import com.beka.jaqsyadet.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HabitListUiState>(HabitListUiState.Loading)
    val uiState: StateFlow<HabitListUiState> = _uiState

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            repository.getAllHabits()
                .catch { error ->
                    _uiState.value = HabitListUiState.Error(error.message ?: "Unknown error")
                }
                .collect { habits ->
                    _uiState.value = HabitListUiState.Success(habits)
                }
        }
    }

    fun markHabitAsCompleted(habitId: Long) {
        viewModelScope.launch {
            try {
                repository.markHabitAsCompleted(habitId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class HabitListUiState {
    data object Loading : HabitListUiState()
    data class Success(val habits: List<Habit>) : HabitListUiState()
    data class Error(val message: String) : HabitListUiState()
} 