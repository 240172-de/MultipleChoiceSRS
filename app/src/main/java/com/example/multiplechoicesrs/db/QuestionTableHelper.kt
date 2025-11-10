package com.example.multiplechoicesrs.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.QuestionJson
import com.example.multiplechoicesrs.model.QuestionResult
import com.example.multiplechoicesrs.model.QuestionStatus

class QuestionTableHelper(context: Context) {
    private val dbHelper = DBHelper(context)

    fun getQuestions(deckId: Int): List<Question> {
        val params = arrayOf(deckId.toString())
        return getQuestions("${DBHelper.DECK_ID} = ?", params)
    }

    fun getQuestions(deckId: Int, categoryIdList: List<Int>): List<Question> {
        val temp = ArrayList<String>(categoryIdList.size + 1)
        temp.add(deckId.toString())
        temp.addAll(categoryIdList.map { it.toString() })
        val params = temp.toTypedArray()

        val inClause = categoryIdList.joinToString {
            "?"
        }

        return getQuestions("${DBHelper.DECK_ID} = ? AND ${DBHelper.CATEGORY_ID} IN ($inClause)", params)
    }

    @SuppressLint("Range")
    private fun getQuestions(whereClause: String, whereParams: Array<String>): List<Question> {
        val list = mutableListOf<Question>()
        val tableWithJoin: String = DBHelper.TABLE_QUESTION +
                " INNER JOIN " + DBHelper.TABLE_QUESTION_RESULT +
                " ON " + DBHelper.TABLE_QUESTION + "." + DBHelper.QUESTION_ID + " = " + DBHelper.TABLE_QUESTION_RESULT + "." + DBHelper.QUESTION_ID

        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(
                tableWithJoin,
                null,
                whereClause,
                whereParams,
                null,
                null,
                null
            )

            cursor.use {
                while (it.moveToNext()) {
                    val result = QuestionResult(
                        it.getInt(it.getColumnIndex(DBHelper.QUESTION_ID)),
                        it.getInt(it.getColumnIndex(DBHelper.NUM_CORRECT)),
                        it.getString(it.getColumnIndex(DBHelper.DATE_DUE)),
                        QuestionStatus.get(it.getInt(it.getColumnIndex(DBHelper.STATUS))),
                        it.getInt(it.getColumnIndex(DBHelper.BOX)),
                    )

                    list.add(
                        Question(
                            it.getInt(it.getColumnIndex(DBHelper.DECK_ID)),
                            it.getInt(it.getColumnIndex(DBHelper.CATEGORY_ID)),
                            it.getInt(it.getColumnIndex(DBHelper.QUESTION_ID)),
                            it.getString(it.getColumnIndex(DBHelper.QUESTION)),
                            getImageBitmap(it.getString(it.getColumnIndex(DBHelper.QUESTION_IMAGE))),
                            it.getString(it.getColumnIndex(DBHelper.ANSWER1)),
                            it.getString(it.getColumnIndex(DBHelper.ANSWER2)),
                            it.getString(it.getColumnIndex(DBHelper.ANSWER3)),
                            it.getString(it.getColumnIndex(DBHelper.ANSWER4)),
                            getImageBitmap(it.getString(it.getColumnIndex(DBHelper.ANSWER1_IMAGE))),
                            getImageBitmap(it.getString(it.getColumnIndex(DBHelper.ANSWER2_IMAGE))),
                            getImageBitmap(it.getString(it.getColumnIndex(DBHelper.ANSWER3_IMAGE))),
                            getImageBitmap(it.getString(it.getColumnIndex(DBHelper.ANSWER4_IMAGE))),
                            it.getInt(it.getColumnIndex(DBHelper.CORRECT_ANSWER)),
                            it.getString(it.getColumnIndex(DBHelper.EXPLANATION)),
                            it.getString(it.getColumnIndex(DBHelper.SOURCE)),
                            result
                        )
                    )
                }
            }
        }

        return list
    }

    private fun getImageBitmap(base64String: String): ImageBitmap? {
        if (base64String.isEmpty()) {
            return null
        }

        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        return bitmap.asImageBitmap()
    }

    fun saveQuestion(question: QuestionJson) {
        dbHelper.writableDatabase.use {  db ->
            val values = ContentValues().apply {
                put(DBHelper.DECK_ID, question.deckId)
                put(DBHelper.CATEGORY_ID, question.categoryId)
                put(DBHelper.QUESTION_ID, question.questionId)
                put(DBHelper.QUESTION, question.question)
                put(DBHelper.QUESTION_IMAGE, question.questionImage)
                put(DBHelper.ANSWER1, question.answer1)
                put(DBHelper.ANSWER2, question.answer2)
                put(DBHelper.ANSWER3, question.answer3)
                put(DBHelper.ANSWER4, question.answer4)
                put(DBHelper.ANSWER1_IMAGE, question.answer1Image)
                put(DBHelper.ANSWER2_IMAGE, question.answer2Image)
                put(DBHelper.ANSWER3_IMAGE, question.answer3Image)
                put(DBHelper.ANSWER4_IMAGE, question.answer4Image)
                put(DBHelper.CORRECT_ANSWER, question.correctAnswer)
                put(DBHelper.EXPLANATION, question.explanation)
                put(DBHelper.SOURCE, question.source)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_QUESTION,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )

            val valuesQuestionResult = ContentValues().apply {
                put(DBHelper.QUESTION_ID, question.questionId)
                put(DBHelper.NUM_CORRECT, 0)
                put(DBHelper.DATE_DUE, "")
                put(DBHelper.STATUS, QuestionStatus.NEW.ordinal)
                put(DBHelper.BOX, 0)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_QUESTION_RESULT,
                null,
                valuesQuestionResult,
                SQLiteDatabase.CONFLICT_IGNORE
            )
        }
    }

    fun deleteQuestion(questionId: Int) {
        dbHelper.writableDatabase.use { db ->
            val params = arrayOf(questionId.toString())

            db.delete(
                DBHelper.TABLE_QUESTION,
                "${DBHelper.QUESTION_ID} = ?",
                params
            )
        }
    }
}