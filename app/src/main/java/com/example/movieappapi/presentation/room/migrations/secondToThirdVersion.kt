package com.example.movieappapi.presentation.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table movieEntity add column tag text not null default 'popular'")
        database.execSQL("update movieEntity set tag = 'popular'")
    }
}