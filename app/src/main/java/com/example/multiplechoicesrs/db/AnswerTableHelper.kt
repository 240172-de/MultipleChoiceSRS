package com.example.multiplechoicesrs.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.multiplechoicesrs.ext.toBool
import com.example.multiplechoicesrs.model.Answer

class AnswerTableHelper (context: Context) {
    private val dbHelper = DBHelper(context)

    fun saveAnswer(answer: Answer) {
        dbHelper.writableDatabase.use {  db ->
            val values = ContentValues().apply {
                put(DBHelper.QUESTION_ID, answer.questionId)
                put(DBHelper.TIMESTAMP, answer.timestamp)
                put(DBHelper.ANSWER_GIVEN, answer.answerGiven)
                put(DBHelper.IS_CORRECT, answer.isCorrect)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_ANSWER,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    @SuppressLint("Range")
    fun getAnswers(questionId: Int): List<Answer> {
        val list = mutableListOf<Answer>()
        val params = arrayOf(questionId.toString())
        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(
                DBHelper.TABLE_ANSWER,
                null,
                "${DBHelper.QUESTION_ID} = ?",
                params,
                null,
                null,
                null
            )

            cursor.use {
                while (it.moveToNext()) {
                    list.add(
                        Answer(
                            it.getInt(it.getColumnIndex(DBHelper.ANSWER_ID)),
                            it.getInt(it.getColumnIndex(DBHelper.QUESTION_ID)),
                            it.getString(it.getColumnIndex(DBHelper.TIMESTAMP)),
                            it.getInt(it.getColumnIndex(DBHelper.ANSWER_GIVEN)),
                            it.getInt(it.getColumnIndex(DBHelper.IS_CORRECT)).toBool(),
                        )
                    )
                }
            }
        }

        return list
    }
}