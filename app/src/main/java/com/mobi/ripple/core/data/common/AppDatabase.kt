package com.mobi.ripple.core.data.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobi.ripple.core.data.common.converter.InstantConverter
import com.mobi.ripple.core.data.post.data_source.local.PostDao
import com.mobi.ripple.core.data.post.data_source.local.PostEntity
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostDao
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostEntity

@Database(
    entities = [SimplePostEntity::class, PostEntity::class],
    version = 3
)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val simplePostDao: SimplePostDao
    abstract val postDao: PostDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "ripple-db.db"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}