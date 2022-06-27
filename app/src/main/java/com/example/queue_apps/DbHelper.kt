package com.example.queue_apps

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import kotlin.jvm.Throws

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertCard(card: CardModel):Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DbContract.CardEntry.COLUMN_ID, card.id)
        values.put(DbContract.CardEntry.COLUMN_NOMOR_KARTU_PENGUNJUNG, card.no_kartu_pengunjung)
        values.put(DbContract.CardEntry.COLUMN_NAMA, card.nama)
        values.put(DbContract.CardEntry.COLUMN_JENIS_KELAMIN, card.jenis_kelamin)
        values.put(DbContract.CardEntry.COLUMN_WAKTU_MASUK, card.waktu_masuk)
        values.put(DbContract.CardEntry.COLUMN_WAKTU_KELUAR, card.waktu_keluar)

        val newRowId = db.insert(DbContract.CardEntry.TABLE_NAME, null, values)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteCard(no_kartu: String):Boolean {
        val db = writableDatabase
        val selection = DbContract.CardEntry.COLUMN_NOMOR_KARTU_PENGUNJUNG+ " LIKE ?"
        val selectionArgs = arrayOf(no_kartu)
        db.delete(DbContract.CardEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun updateCard(waktuKeluar: String, id: String):Boolean {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DbContract.CardEntry.COLUMN_WAKTU_KELUAR, waktuKeluar)
        db.update(DbContract.CardEntry.TABLE_NAME, contentValues, "id=?", arrayOf(id))
        return true
    }

    fun findCardByCardNumber(id: String): ArrayList<CardModel> {
        val cards = ArrayList<CardModel>()
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM "+ DbContract.CardEntry.TABLE_NAME+
                " WHERE "+ DbContract.CardEntry.COLUMN_ID +"= ? "
        val args = arrayOf(id)

        var id: String
        var no_kartu_pengunjung: String
        var nama: String
        var jenis_kelamin: String
        var waktu_masuk: String
        var waktu_keluar: String

        db.rawQuery(selectQuery, args).use {
            if (it.moveToFirst()) {
                id = it.getString(it.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_ID))
                no_kartu_pengunjung = it.getString(it.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_NOMOR_KARTU_PENGUNJUNG))
                nama = it.getString(it.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_NAMA))
                jenis_kelamin = it.getString(it.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_JENIS_KELAMIN))
                waktu_masuk = it.getString(it.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_WAKTU_MASUK))
                waktu_keluar = it.getString(it.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_WAKTU_KELUAR))
                cards.add(CardModel(id, no_kartu_pengunjung,nama, jenis_kelamin, waktu_masuk, waktu_keluar))
            }
        }
        return cards
    }

    fun readAllCards(type: String): ArrayList<CardModel> {
        val cards = ArrayList<CardModel>()
        val db = writableDatabase
        var cursor:Cursor? = null
        var isNullValidation = ""

        if (type === "masuk") {
            isNullValidation = "="
        } else {
            isNullValidation = "!="
        }

        try {
            cursor = db.rawQuery("select * from "+ DbContract.CardEntry.TABLE_NAME +
                    " WHERE "+ DbContract.CardEntry.COLUMN_WAKTU_KELUAR + " "+isNullValidation
                + " '' ORDER BY "+DbContract.CardEntry.COLUMN_NOMOR_KARTU_PENGUNJUNG, null)
        } catch (e: SQLException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: String
        var no_kartu_pengunjung: String
        var nama: String
        var jenis_kelamin: String
        var waktu_masuk: String
        var waktu_keluar: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_ID))
                no_kartu_pengunjung = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_NOMOR_KARTU_PENGUNJUNG))
                nama = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_NAMA))
                jenis_kelamin = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_JENIS_KELAMIN))
                waktu_masuk = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_WAKTU_MASUK))
                waktu_keluar = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.CardEntry.COLUMN_WAKTU_KELUAR))

                cards.add(CardModel(id, no_kartu_pengunjung,nama, jenis_kelamin, waktu_masuk, waktu_keluar))
                cursor.moveToNext()
            }
        }
        return cards
    }

    companion object {
        val DATABASE_VERSION = 2
        val DATABASE_NAME = "CardQueue.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ DbContract.CardEntry.TABLE_NAME + " (" +
                    DbContract.CardEntry.COLUMN_ID+" TEXT PRIMARY KEY, " +
                    DbContract.CardEntry.COLUMN_NOMOR_KARTU_PENGUNJUNG+" TEXT, " +
                    DbContract.CardEntry.COLUMN_NAMA+" TEXT, " +
                    DbContract.CardEntry.COLUMN_JENIS_KELAMIN+" TEXT, " +
                    DbContract.CardEntry.COLUMN_WAKTU_MASUK+" TEXT, " +
                    DbContract.CardEntry.COLUMN_WAKTU_KELUAR+" TEXT)"
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+DbContract.CardEntry.TABLE_NAME
    }
}