package com.example.multiplechoicesrs.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.multiplechoicesrs.model.Category

class CategoryTableHelper(context: Context) {
    private val dbHelper = DBHelper(context)

    @SuppressLint("Range")
    fun getCategories(deckId: Int): List<Category> {
        val list = mutableListOf<Category>()
        val params = arrayOf(deckId.toString())

        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(
                DBHelper.TABLE_CATEGORY,
                null,
                "${DBHelper.DECK_ID} = ?",
                params,
                null,
                null,
                null
            )

            cursor.use {
                while (it.moveToNext()) {
                    list.add(
                        Category(
                            it.getInt(it.getColumnIndex(DBHelper.DECK_ID)),
                            it.getInt(it.getColumnIndex(DBHelper.CATEGORY_ID)),
                            it.getString(it.getColumnIndex(DBHelper.CATEGORY_NAME))
                        )
                    )
                }
            }
        }

        return list
    }

    fun saveCategory(category: Category) {
        dbHelper.writableDatabase.use {  db ->
            val values = ContentValues().apply {
                put(DBHelper.DECK_ID, category.deckId)
                put(DBHelper.CATEGORY_ID, category.categoryId)
                put(DBHelper.CATEGORY_NAME, category.name)
            }

            db.insertWithOnConflict(
                DBHelper.TABLE_CATEGORY,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    fun deleteCategory(categoryId: Int) {
        dbHelper.writableDatabase.use { db ->
            val params = arrayOf(categoryId.toString())

            db.delete(
                DBHelper.TABLE_CATEGORY,
                "${DBHelper.CATEGORY_ID} = ?",
                params
            )

            db.delete(
                DBHelper.TABLE_QUESTION,
                "${DBHelper.CATEGORY_ID} = ?",
                params
            )
        }
    }
}