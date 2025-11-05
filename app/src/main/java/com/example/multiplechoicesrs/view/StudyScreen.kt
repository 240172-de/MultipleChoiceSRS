package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.logic.StudyHelper
import com.example.multiplechoicesrs.model.Deck

@Composable
fun StudyScreen(
    deck: Deck,
    categoryIdList: List<Int>,
    numToStudy: Int,
    navToDeckList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val studyHelper = StudyHelper(LocalContext.current)

    ProvideAppBarTitle {
        Text("学習")
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            onClick = {
                finishStudySession(navToDeckList)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "戻る"
            )
        }
    }

    Column(modifier) {
        Text("${deck.name}   $categoryIdList    $numToStudy")

        Text("${studyHelper.getQuestions(deck.deckId, categoryIdList, numToStudy)}")
    }
}

private fun finishStudySession(navToDeckList: () -> Unit) {
    //TODO: Display Results
    navToDeckList()
}