package com.mobi.ripple.core.data.data_source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobi.ripple.core.data.data_source.local.profile.SimplePostDao
import com.mobi.ripple.core.data.data_source.local.profile.SimplePostEntity

@Database(
    entities = [SimplePostEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val simplePostDao: SimplePostDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "ripple-db.db"
                ).build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}