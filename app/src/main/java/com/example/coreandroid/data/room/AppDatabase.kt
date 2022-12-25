package com.example.coreandroid.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coreandroid.data.room.user.User
import com.example.coreandroid.data.room.user.UserDao

//Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [User::class], version = 2, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        //Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            //if the INSTANCE is not null, them return it, it if is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "login-app"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}