package com.beka.jaqsyadet.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beka.jaqsyadet.data.model.HabitCheck
import com.beka.jaqsyadet.ui.viewmodel.HabitDetailUiState
import com.beka.jaqsyadet.ui.viewmodel.HabitDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val checks by viewModel.checks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Habit Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.markAsCompleted() }) {
                Icon(Icons.Default.Check, contentDescription = "Complete")
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is HabitDetailUiState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HabitDetailUiState.Success -> {
                val habit = (uiState as HabitDetailUiState.Success).habit
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = habit.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = habit.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "🔥",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(
                                            text = "${habit.streak}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Current Streak",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "✓",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(
                                            text = "${habit.totalCompletions}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Total Completions",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "History",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    if (checks.isEmpty()) {
                        item {
                            Text(
                                text = "No completions yet",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                            )
                        }
                    } else {
                        items(checks) { check ->
                            HabitCheckItem(check = check)
                        }
                    }
                }
            }
            is HabitDetailUiState.Error -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text((uiState as HabitDetailUiState.Error).message)
                }
            }
        }
    }
}

@Composable
fun HabitCheckItem(
    check: HabitCheck,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = dateFormat.format(Date(check.completedAt)),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 