package com.example.multiplechoicesrs.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "multiple_choice_srs.sqlite"
        private const val DB_VERSION = 1

        const val TABLE_DECK = "decks"
        const val TABLE_CATEGORY = "categories"
        const val TABLE_QUESTION = "questions"
        const val TABLE_QUESTION_RESULT = "question_results"
        const val TABLE_ANSWER = "answers"
        const val TABLE_STUDY_SESSION = "study_sessions"

        const val DECK_ID = "deck_id"
        const val DECK_NAME = "name"
        const val VERSION_ID = "version_id"

        const val CATEGORY_ID = "category_id"
        const val CATEGORY_NAME = "name"

        const val QUESTION_ID = "question_id"
        const val QUESTION = "question"
        const val QUESTION_IMAGE = "question_image"
        const val ANSWER1 = "answer1"
        const val ANSWER2 = "answer2"
        const val ANSWER3 = "answer3"
        const val ANSWER4 = "answer4"
        const val ANSWER1_IMAGE = "answer1_image"
        const val ANSWER2_IMAGE = "answer2_image"
        const val ANSWER3_IMAGE = "answer3_image"
        const val ANSWER4_IMAGE = "answer4_image"
        const val CORRECT_ANSWER = "correct_answer"
        const val EXPLANATION = "explanation"
        const val SOURCE = "source"

        const val NUM_CORRECT = "num_correct"
        const val DATE_DUE = "date_due"
        const val STATUS = "status"
        const val BOX = "box"

        const val ANSWER_ID = "answer_id"
        const val TIMESTAMP = "timestamp"
        const val ANSWER_GIVEN = "answer_given"
        const val IS_CORRECT = "is_correct"

        const val STUDY_SESSION_ID = "study_session_id"
        const val NUM_INCORRECT = "num_incorrect"

        private const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS"
        private const val TEXT = "TEXT"
        private const val INTEGER = "INTEGER"
        private const val PRIM_KEY = "PRIMARY KEY"
        private const val PRIM_KEY_AUTOINC = "PRIMARY KEY AUTOINCREMENT"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.let {
            it.execSQL(generateSQLTableDeck())
            it.execSQL(generateSQLTableCategory())
            it.execSQL(generateSQLTableQuestion())
            it.execSQL(generateSQLTableQuestionResult())
            it.execSQL(generateSQLTableAnswer())
            it.execSQL(generateSQLTableStudySession())
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    private fun generateSQLTableDeck(): String {
        return "$CREATE_TABLE $TABLE_DECK(" +
                    "$DECK_ID $INTEGER $PRIM_KEY," +
                    "$DECK_NAME $TEXT," +
                    "$VERSION_ID $INTEGER" +
                ")"
    }

    private fun generateSQLTableCategory(): String {
        return "$CREATE_TABLE $TABLE_CATEGORY(" +
                    "$DECK_ID $INTEGER," +
                    "$CATEGORY_ID $INTEGER $PRIM_KEY," +
                    "$CATEGORY_NAME $TEXT," +
                    onDeleteCascade(DECK_ID, TABLE_DECK) +
                ")"
    }

    private fun generateSQLTableQuestion(): String {
        return "$CREATE_TABLE $TABLE_QUESTION(" +
                    "$DECK_ID $INTEGER," +
                    "$CATEGORY_ID $INTEGER," +
                    "$QUESTION_ID $INTEGER $PRIM_KEY," +
                    "$QUESTION $TEXT," +
                    "$QUESTION_IMAGE $TEXT," +
                    "$ANSWER1 $TEXT," +
                    "$ANSWER2 $TEXT," +
                    "$ANSWER3 $TEXT," +
                    "$ANSWER4 $TEXT," +
                    "$ANSWER1_IMAGE $TEXT," +
                    "$ANSWER2_IMAGE $TEXT," +
                    "$ANSWER3_IMAGE $TEXT," +
                    "$ANSWER4_IMAGE $TEXT," +
                    "$CORRECT_ANSWER $INTEGER," +
                    "$EXPLANATION $TEXT," +
                    "$SOURCE $TEXT," +
                    onDeleteCascade(DECK_ID, TABLE_DECK) + "," +
                    onDeleteCascade(CATEGORY_ID, TABLE_CATEGORY) +
                ")"
    }

    private fun generateSQLTableQuestionResult(): String {
        return "$CREATE_TABLE $TABLE_QUESTION_RESULT(" +
                    "$QUESTION_ID $INTEGER $PRIM_KEY," +
                    "$DATE_DUE $TEXT," +
                    "$STATUS $INTEGER," +
                    "$BOX $INTEGER," +
                    onDeleteCascade(QUESTION_ID, TABLE_QUESTION) +
                ")"
    }

    private fun generateSQLTableAnswer(): String {
        return "$CREATE_TABLE $TABLE_ANSWER(" +
                    "$ANSWER_ID $INTEGER $PRIM_KEY_AUTOINC," +
                    "$QUESTION_ID $INTEGER," +
                    "$TIMESTAMP $TEXT," +
                    "$ANSWER_GIVEN $INTEGER," +
                    "$IS_CORRECT $INTEGER," +
                    onDeleteCascade(QUESTION_ID, TABLE_QUESTION) +
                ")"
    }

    private fun generateSQLTableStudySession(): String {
        return "$CREATE_TABLE $TABLE_STUDY_SESSION(" +
                    "$STUDY_SESSION_ID $INTEGER $PRIM_KEY_AUTOINC," +
                    "$TIMESTAMP $TEXT," +
                    "$NUM_CORRECT $INTEGER," +
                    "$NUM_INCORRECT $INTEGER" +
                ")"
    }

    private fun onDeleteCascade(foreignKey: String, referencesTable: String): String {
        return "FOREIGN KEY($foreignKey) REFERENCES $referencesTable($foreignKey) ON DELETE CASCADE"
    }
}