package com.example.multiplechoicesrs.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.multiplechoicesrs.model.StudySession

class StudySessionTableHelper(context: Context) {
    private val dbHelper = DBHelper(context)

    fun saveStudySession(studySession: StudySession) {
        dbHelper.writableDatabase.use {  db ->
            val values = ContentValues().apply {
                put(DBHelper.TIMESTAMP, studySession.timestamp)
                put(DBHelper.NUM_CORRECT_FIRST, studySession.numCorrectFirst)
                put(DBHelper.NUM_INCORRECT_FIRST, studySession.numIncorrectFirst)
                put(DBHelper.NUM_CORRECT_TOTAL, studySession.numCorrectTotal)
                put(DBHelper.NUM_INCORRECT_TOTAL, studySession.numIncorrectTotal)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_STUDY_SESSION,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }
}