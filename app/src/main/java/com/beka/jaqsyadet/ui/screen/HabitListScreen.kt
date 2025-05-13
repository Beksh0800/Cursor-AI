package com.beka.jaqsyadet.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaqsyadet.R
import com.beka.jaqsyadet.data.model.Habit
import com.beka.jaqsyadet.ui.viewmodel.HabitListUiState
import com.beka.jaqsyadet.ui.viewmodel.HabitListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    onHabitClick: (Long) -> Unit,
    onAddHabitClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabitClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is HabitListUiState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HabitListUiState.Success -> {
                val habits = (uiState as HabitListUiState.Success).habits
                if (habits.isEmpty()) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_habits))
                    }
                } else {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(habits) { habit ->
                            HabitItem(
                                habit = habit,
                                onHabitClick = onHabitClick,
                                onCompleteClick = { viewModel.markHabitAsCompleted(it) }
                            )
                        }
                    }
                }
            }
            is HabitListUiState.Error -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text((uiState as HabitListUiState.Error).message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitItem(
    habit: Habit,
    onHabitClick: (Long) -> Unit,
    onCompleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onHabitClick(habit.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = habit.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 ${habit.streak}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "✓ ${habit.totalCompletions}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            IconButton(onClick = { onCompleteClick(habit.id) }) {
                Icon(Icons.Default.Check, contentDescription = "Complete")
            }
        }
    }
} 