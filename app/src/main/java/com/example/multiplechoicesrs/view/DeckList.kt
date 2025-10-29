package com.example.multiplechoicesrs.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Deck

@Composable
fun DeckList(
    decks: List<Deck>,
    navToCategoryList: (deckId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(decks) { deck ->
            DeckItem(deck, navToCategoryList)
        }
    }
}

@Composable
fun DeckItem(
    deck: Deck,
    navToCategoryList: (deckId: Int) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    ) {
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
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedButton(
                    onClick = {
                        //TODO: AnalysisScreen
                        Log.d("TEST", "Show Analysis Screen")
                    },
                    modifier = Modifier.size(46.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(15)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_bar_chart_24),
                        contentDescription = "分析",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(36.dp)
                    )
                }
            }

            Column {
                Text(
                    "学習",
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

                        }) {
                        Text("全分野")
                    }

                    //TODO: Show only if there are multiple categories
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navToCategoryList(deck.deckId)
                        }) {
                        Text("分野から選ぶ")
                    }
                }
            }
        }
    }
}