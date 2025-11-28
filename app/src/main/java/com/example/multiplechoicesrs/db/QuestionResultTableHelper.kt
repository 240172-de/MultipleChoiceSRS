package com.example.multiplechoicesrs.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.multiplechoicesrs.model.QuestionResult
import com.example.multiplechoicesrs.model.QuestionStatus

class QuestionResultTableHelper(context: Context) {
    private val dbHelper = DBHelper(context)

    fun saveQuestionResult(questionResult: QuestionResult) {
        dbHelper.writableDatabase.use {  db ->
            val values = ContentValues().apply {
                put(DBHelper.QUESTION_ID, questionResult.questionId)
                put(DBHelper.DATE_DUE, questionResult.dateDue)
                put(DBHelper.STATUS, questionResult.status.ordinal)
                put(DBHelper.BOX, questionResult.box)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_QUESTION_RESULT,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    @SuppressLint("Range")
    fun getQuestionResult(questionId: Int): QuestionResult? {
        val params = arrayOf(questionId.toString())
        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(
                DBHelper.TABLE_QUESTION_RESULT,
                null,
                "${DBHelper.QUESTION_ID} = ?",
                params,
                null,
                null,
                null
            )

            cursor.use {
                while (it.moveToFirst()) {
                    return QuestionResult(
                        it.getInt(it.getColumnIndex(DBHelper.QUESTION_ID)),
                        it.getString(it.getColumnIndex(DBHelper.DATE_DUE)),
                        QuestionStatus.get(it.getInt(it.getColumnIndex(DBHelper.STATUS))),
                        it.getInt(it.getColumnIndex(DBHelper.BOX)),
                    )
                }
            }
        }

        return null
    }
}