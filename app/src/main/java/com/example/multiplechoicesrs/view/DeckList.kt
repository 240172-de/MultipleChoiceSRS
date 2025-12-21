package com.example.multiplechoicesrs.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.ext.hasDueReviews
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.QuestionStatus
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.LightBlue
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import com.example.multiplechoicesrs.ui.theme.YellowWaring
import com.example.multiplechoicesrs.view.custom.CustomCard
import com.example.multiplechoicesrs.view.dialog.SelectNumToStudyDialog

@Composable
fun DeckList(
    decks: List<Deck>,
    navToCategoryList: (deck: Deck) -> Unit,
    navToStudy: (deck: Deck, categoryIdList: List<Int>, numToStudy: Int) -> Unit,
    navToAnalysis: (deck: Deck) -> Unit
) {
    var selectedDeck: Deck? by remember { mutableStateOf(null) }
    var selectedCategoryIdList: List<Int> by remember { mutableStateOf(emptyList()) }
    var showSelectNumDialog by remember { mutableStateOf(false) }

    if (showSelectNumDialog && selectedDeck != null) {
        SelectNumToStudyDialog(
            deck = selectedDeck!!,
            categoryIdList = selectedCategoryIdList,
            onDismissRequest = {
                showSelectNumDialog = false
            },
            navToStudy = navToStudy)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(decks) { deck ->
            DeckItem(deck, navToCategoryList, navToAnalysis) { deck, categoryIdList ->
                selectedCategoryIdList = categoryIdList
                selectedDeck = deck
                showSelectNumDialog = true
            }
        }
    }
}

@Composable
fun DeckItem(
    deck: Deck,
    navToCategoryList: (deck: Deck) -> Unit,
    navToAnalysis: (Deck) -> Unit,
    onClickStudy: (deck: Deck, categoryIdList: List<Int>) -> Unit,
) {
    CustomCard {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Spacer(Modifier
                .width(8.dp)
                .fillMaxHeight()
                .background(getStatusColor(LocalContext.current, deck.deckId))
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        deck.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f, false)
                    )

                    OutlinedButton(
                        onClick = {
                            navToAnalysis(deck)
                        },
                        modifier = Modifier.size(46.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(15)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_bar_chart_24),
                            contentDescription = stringResource(R.string.analysis),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }
                }

                Column {
                    Text(
                        stringResource(R.string.study_scope),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val categoryIdList =
                                    deck.categories?.map { it.categoryId } ?: emptyList()
                                onClickStudy(deck, categoryIdList)
                            }) {
                            Text(stringResource(R.string.all_categories))
                        }

                        if (deck.categories != null && deck.categories!!.size > 1) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navToCategoryList(deck)
                                }) {
                                Text(stringResource(R.string.choose_from_category))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getStatusColor(context: Context, deckId: Int): Color {
    val questionTableHelper = QuestionTableHelper(context)
    val questionList = questionTableHelper.getQuestions(deckId)

    return if (questionList.any { it.result.status == QuestionStatus.RELEARN }) {
        RedIncorrectAnswer
    } else if (questionList.hasDueReviews()) {
        YellowWaring
    } else if (questionList.any { it.result.status == QuestionStatus.NEW }) {
        LightBlue
    } else {
        GreenCorrectAnswer
    }
}