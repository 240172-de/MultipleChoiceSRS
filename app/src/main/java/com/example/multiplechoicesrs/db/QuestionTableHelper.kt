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

class QuestionTableHelper(context: Context) {
    private val dbHelper = DBHelper(context)

    fun getQuestions(deckId: Int): List<Question> {
        val params = arrayOf(deckId.toString())
        return getQuestions("${DBHelper.DECK_ID} = ?", params)
    }

    fun getQuestions(deckId: Int, categoryId: Int): List<Question> {
        val params = arrayOf(
            deckId.toString(),
            categoryId.toString()
        )

        return getQuestions("${DBHelper.DECK_ID} = ? AND ${DBHelper.CATEGORY_ID} = ?", params)
    }

    @SuppressLint("Range")
    private fun getQuestions(whereClause: String, whereParams: Array<String>): List<Question> {
        val list = mutableListOf<Question>()
        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(
                DBHelper.TABLE_QUESTION,
                null,
                whereClause,
                whereParams,
                null,
                null,
                null
            )

            cursor.use {
                while (it.moveToNext()) {
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
                        )
                    )
                }
            }
        }

        return list
    }

    private fun getImageBitmap(base64String: String): ImageBitmap {
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