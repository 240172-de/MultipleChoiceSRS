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
                put(DBHelper.NUM_CORRECT, studySession.numCorrect)
                put(DBHelper.NUM_INCORRECT, studySession.numIncorrect)
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