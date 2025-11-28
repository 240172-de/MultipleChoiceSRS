package com.example.multiplechoicesrs.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.multiplechoicesrs.model.Deck

class DeckTableHelper(context: Context) {
    private val dbHelper = DBHelper(context)

    @SuppressLint("Range")
    fun getDecks(): List<Deck> {
        val list = mutableListOf<Deck>()
        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(
                DBHelper.TABLE_DECK,
                null,
                null,
                null,
                null,
                null,
                null
            )

            cursor.use {
                while (it.moveToNext()) {
                    list.add(
                        Deck(
                            it.getInt(it.getColumnIndex(DBHelper.DECK_ID)),
                            it.getString(it.getColumnIndex(DBHelper.DECK_NAME)),
                            it.getInt(it.getColumnIndex(DBHelper.VERSION_ID))
                        )
                    )
                }
            }
        }

        return list
    }

    fun saveDeck(deck: Deck) {
        dbHelper.writableDatabase.use {  db ->
            val values = ContentValues().apply {
                put(DBHelper.DECK_ID, deck.deckId)
                put(DBHelper.DECK_NAME, deck.name)
                put(DBHelper.VERSION_ID, deck.versionId)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_DECK,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    fun deleteDeck(deckId: Int) {
        dbHelper.writableDatabase.use { db ->
            val params = arrayOf(deckId.toString())

            db.delete(
                DBHelper.TABLE_DECK,
                "${DBHelper.DECK_ID} = ?",
                params
            )
        }
    }
}