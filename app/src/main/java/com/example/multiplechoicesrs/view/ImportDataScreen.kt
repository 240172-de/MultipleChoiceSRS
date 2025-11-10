package com.example.multiplechoicesrs.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.Category
import com.example.multiplechoicesrs.model.Deck
import com.example.multiplechoicesrs.model.DecksJson
import com.example.multiplechoicesrs.model.ImportDecksUiState
import com.example.multiplechoicesrs.model.ImportDecksViewModel
import com.example.multiplechoicesrs.model.QuestionJson

@Composable
fun ImportDataScreen(
    navBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deckTableHelper = DeckTableHelper(LocalContext.current)
    val categoryTableHelper = CategoryTableHelper(LocalContext.current)
    val questionTableHelper = QuestionTableHelper(LocalContext.current)

    ProvideAppBarTitle {
        Text("インポート")
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

    Column(modifier = modifier) {
        Button(onClick = {
            deckTableHelper.saveDeck(
                Deck(
                    1,
                    "応用情報技術者試験",
                    1
                )
            )

            deckTableHelper.saveDeck(
                Deck(
                    2,
                    "日本語能力試験",
                    1
                )
            )

            categoryTableHelper.saveCategory(
                Category(
                    1,
                    1,
                    "基礎理論"
                )
            )

            categoryTableHelper.saveCategory(
                Category(
                    1,
                    2,
                    "アルゴリズムとプログラミング"
                )
            )

            questionTableHelper.saveQuestion(
                QuestionJson(
                    1,
                    1,
                    1,
                    "M/M/1 の待ち行列モデルにおいて，窓口の利用率が25% から40% に増えると，平均待ち時間は何倍になるか。",
                    "",
                    "1.25",
                    "1.6",
                    "2",
                    "3",
                    "",
                    "",
                    "",
                    "",
                    3,
                    "",
                    "令和6年秋期問1",
                )
            )

            questionTableHelper.saveQuestion(
                QuestionJson(
                    1,
                    1,
                    2,
                    "AI における教師あり学習での交差検証に関する記述はどれか。",
                    "",
                    "過学習を防ぐために，回帰モデルに複雑さを表すペナルティ項を加え，訓練データへ過剰に適合しないようにモデルを調整する",
                    "学習の精度を高めるために，複数の異なるアルゴリズムのモデルで学習し，学習の結果は組み合わせて評価する。",
                    "学習モデルの汎化性能を高めるために，単一のモデルで関連する複数の課題を学習することによって，課題間に共通する要因を獲得する。",
                    "学習モデルの汎化性能を評価するために，データを複数のグループに分割し，一部を学習に残りを評価に使い，順にグループを入れ替えて学習と評価を繰り返す。",
                    "",
                    "",
                    "",
                    "",
                    4,
                    "",
                    "令和6年秋期問2",
                )
            )

            questionTableHelper.saveQuestion(
                QuestionJson(
                    1,
                    2,
                    3,
                    "自然数をキーとするデータを，ハッシュ表を用いて管理する。キーxのハッシュ関数h(x) をh(x) = x mod nとすると，任意のキーa とb が衝突する条件はどれか。ここで，n はハッシュ表の大きさであり，x modn はx をn で割った余りを表す。",
                    "",
                    "a +b がn の倍数",
                    "a−b がn の倍数",
                    "n がa+b の倍数",
                    "n がa−b の倍数",
                    "",
                    "",
                    "",
                    "",
                    2,
                    "",
                    "令和6年秋期問6",
                )
            )
        }) {
            Text("Dummy data")
        }

        val importDecksViewModel: ImportDecksViewModel = viewModel()
        ImportDecksScreen(
            importDecksUiState = importDecksViewModel.importDecksUiState
        )
    }
}

@Composable
fun ImportDecksScreen(
    importDecksUiState: ImportDecksUiState,
    modifier: Modifier = Modifier,
) {
    when (importDecksUiState) {
        is ImportDecksUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is ImportDecksUiState.Success -> ImportDecksListScreen(
            importDecksUiState.decks, modifier = modifier.fillMaxWidth()
        )
        is ImportDecksUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ImportDecksListScreen(decks: DecksJson, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("$decks")
        Log.d("TEST", "$decks")
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment= Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .background(White, shape = RoundedCornerShape(8.dp))
    ) {
        CircularProgressIndicator()
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text("Load failed", modifier = Modifier.padding(16.dp))
    }
}