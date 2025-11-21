package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.view.custom.ExpandableView
import com.example.multiplechoicesrs.view.custom.ProvideAppBarNavigationIcon
import com.example.multiplechoicesrs.view.custom.ProvideAppBarTitle

@Composable
fun AnalysisScreen(
    deck: Deck,
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProvideAppBarTitle {
        Text("分析")
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            onClick = {
                navBack()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "戻る"
            )
        }
    }

    Column(modifier) {
        //Tabs
        //Deck | Category | Question

        //Auf Deck Level:
        //Anteil richtig je Kategorie (genormt auf 1)?
        //Anteil richtig gesamt
        //Anteil richtig erstes Mal/zweites Mal/...?

        //Auf Category Level:
        //Liste der Kategorien (aufklappbar)
        //Pie Anteil Richtig
        //Braucht man das wirklich?
        //Ist ja schon vom Deck aus sehbar

        //Auf Question Level:
        //Liste an Categories (jeweils ausklappbar)
        //Darunter Liste an Fragen (ebenfalls ausklappbar)
        //PieChart Richtig/Falsch dieser Frage

        //Oder vllt. statt Tabs gleich alles ausklappbar?
        //Dann hatte man fur jede Kategorie ein ausklappbares Element
        //Ebenso eins fur "alle Kategorien"
        //Wenn man die Kategorie aufklappt sieht man zunachst eine ubersicht uber alle Fragen in der Kategorie
        //(wieder per "alle Fragen" aufklappbar?)
        //Dann eine Liste der einzelnen Fragen
        //Das wird zu viel fur einen Bildschirm

        //BarChart welche Antworten gegeben
        //Also insgesamt 2 Grafiken

        AnalysisTabManager(deck)
    }
}