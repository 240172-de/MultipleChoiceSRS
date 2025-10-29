package com.example.multiplechoicesrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.db.CategoryTableHelper
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.Category
import com.example.multiplechoicesrs.model.Deck
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
        Text("Generate Testdata")

        Button(onClick = {
            deckTableHelper.saveDeck(Deck(
                1,
                "応用情報技術者試験",
                1
            ))

            deckTableHelper.saveDeck(Deck(
                2,
                "日本語能力試験",
                1
            ))

            categoryTableHelper.saveCategory(Category(
                1,
                1,
                "基礎理論"
            ))

            categoryTableHelper.saveCategory(Category(
                1,
                2,
                "アルゴリズムとプログラミング"
            ))

            questionTableHelper.saveQuestion(QuestionJson(
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
            ))

            questionTableHelper.saveQuestion(QuestionJson(
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
            ))

            questionTableHelper.saveQuestion(QuestionJson(
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
            ))
        }) {
            Text("Import")
        }
    }
}