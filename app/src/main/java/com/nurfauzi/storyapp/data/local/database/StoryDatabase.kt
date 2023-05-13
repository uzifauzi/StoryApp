package com.nurfauzi.storyapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nurfauzi.storyapp.data.local.dao.RemoteKeysDao
import com.nurfauzi.storyapp.data.local.dao.StoryDao
import com.nurfauzi.storyapp.data.local.entity.RemoteKeys
import com.nurfauzi.storyapp.domain.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            if (INSTANCE == null) {
                synchronized(StoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StoryDatabase::class.java, "story_database"
                    )
                        .build()
                }
            }
            return INSTANCE as StoryDatabase
        }
    }
}