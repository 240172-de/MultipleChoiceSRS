package com.example.multiplechoicesrs.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.view.CategoryListScreen
import com.example.multiplechoicesrs.view.ContentAwareTopAppBar
import com.example.multiplechoicesrs.view.DeckListScreen
import com.example.multiplechoicesrs.view.ImportDataScreen
import com.example.multiplechoicesrs.nav.Screen
import com.example.multiplechoicesrs.view.StudyScreen
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            ContentAwareTopAppBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        NavHost(
            startDestination = Screen.DeckListScreen,
            navController = navController,
        ) {
            composable<Screen.ImportDataScreen> {
                ImportDataScreen(
                    navBack = navController::popBackStack,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            composable<Screen.DeckListScreen> {
                DeckListScreen(
                    navToImport = {
                        navController.navigate(Screen.ImportDataScreen)
                    },
                    navToCategoryList = { deckId ->
                        navController.navigate(Screen.CategoryListScreen(deckId))
                    },
                    navToStudy = { deckId, categoryIdList ->
                        navController.navigate(Screen.StudyScreen(deckId, categoryIdList))
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            composable<Screen.CategoryListScreen>(
                typeMap = mapOf(
                    typeOf<Deck>() to navTypeOf<Deck>()
                )
            ) {
                val args = it.toRoute<Screen.CategoryListScreen>()

                CategoryListScreen(
                    deck = args.deck,
                    navToStudy = { deck, categoryIdList ->
                        navController.navigate(Screen.StudyScreen(deck, categoryIdList))
                    },
                    navBack = navController::popBackStack,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            composable<Screen.StudyScreen>(
                typeMap = mapOf(
                    typeOf<Deck>() to navTypeOf<Deck>()
                )
            ) {
                val args = it.toRoute<Screen.StudyScreen>()

                StudyScreen(
                    deck = args.deck,
                    categoryIdList = args.categoryIdList,
                    navToDeckList = {
                        navController.navigate(Screen.DeckListScreen)
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}