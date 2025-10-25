package com.example.multiplechoicesrs.db

import android.annotation.SuppressLint
import android.content.Context
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
}