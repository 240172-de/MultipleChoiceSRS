package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multiplechoicesrs.model.Deck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTabManager(deck: Deck, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = AnalysisDestination.DECK
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Box(modifier = modifier) {
        Column {
            PrimaryTabRow(selectedTabIndex = selectedDestination) {
                AnalysisDestination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        text = {
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = destination.label,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )
                }
            }

            AppNavHost(navController, startDestination, deck, Modifier.padding(vertical = 10.dp, horizontal = 10.dp))
        }
    }
}

enum class AnalysisDestination(
    val route: String,
    val label: String
) {
    DECK("deck", "デッキ"),
    CATEGORY("category", "分野"),
    QUESTION("question", "問題"),
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: AnalysisDestination,
    deck: Deck,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        AnalysisDestination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    AnalysisDestination.DECK -> AnalysisDeck(deck)
                    AnalysisDestination.CATEGORY -> {
                        Text("Category")
                    }
                    AnalysisDestination.QUESTION -> {
                        Text("Question")
                    }
                }
            }
        }
    }
}