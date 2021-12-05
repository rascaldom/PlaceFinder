package com.hyundai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyundai.data.DEFAULT_DATABASE_NAME
import com.hyundai.data.local.dao.PlaceDao
import com.hyundai.data.model.GroupEntity
import com.hyundai.data.model.PlaceEntity

@Database(
    entities = [GroupEntity::class, PlaceEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DEFAULT_DATABASE_NAME).build()
    }

    abstract fun placeDao(): PlaceDao

}