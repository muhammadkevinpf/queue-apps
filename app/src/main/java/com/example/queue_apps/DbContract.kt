package com.example.queue_apps

import android.provider.BaseColumns

object DbContract {
    class CardEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "cards"
            val COLUMN_ID = "id"
            val COLUMN_NOMOR_KARTU_PENGUNJUNG = "nomor_kartu_pengunjung"
            val COLUMN_NAMA = "name"
            val COLUMN_JENIS_KELAMIN = "jenis_kelamin"
            val COLUMN_WAKTU_MASUK = "waktu_masuk"
            val COLUMN_WAKTU_KELUAR = "waktu_keluar"
        }
    }
}