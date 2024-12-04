package com.mobi.ripple.core.data.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobi.ripple.core.data.common.converter.ChatEventConverter
import com.mobi.ripple.core.data.common.converter.InstantConverter
import com.mobi.ripple.core.data.post.data_source.local.PostDao
import com.mobi.ripple.core.data.post.data_source.local.PostEntity
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostDao
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostEntity
import com.mobi.ripple.core.data.reply.data_source.local.ReplyDao
import com.mobi.ripple.core.data.reply.data_source.local.ReplyEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ChatDao
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ChatEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.MessageDao
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.MessageEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ParticipantChatDao
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ParticipantChatEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ParticipantDao
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ParticipantEntity

@Database(
    entities = [
        SimplePostEntity::class,
        PostEntity::class,
        ReplyEntity::class,
        MessageEntity::class,
        ChatEntity::class,
        ParticipantChatEntity::class,
        ParticipantEntity::class
    ],
    version = 3
)
@TypeConverters(InstantConverter::class, ChatEventConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val simplePostDao: SimplePostDao
    abstract val postDao: PostDao
    abstract val replyDao: ReplyDao
    abstract val messageDao: MessageDao
    abstract val chatDao: ChatDao
    abstract val participantChatDao: ParticipantChatDao
    abstract val participantDao: ParticipantDao

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