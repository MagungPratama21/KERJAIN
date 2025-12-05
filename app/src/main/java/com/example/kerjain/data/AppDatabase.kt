package com.example.kerjain.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Pelamar::class,
        Perusahaan::class,
        Lowongan::class,
        Lamaran::class,
        Chat::class,
        Bookmark::class,
        NotificationItem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pelamarDao(): PelamarDao
    abstract fun perusahaanDao(): PerusahaanDao
    abstract fun lowonganDao(): LowonganDao
    abstract fun lamaranDao(): LamaranDao
    abstract fun chatDao(): ChatDao

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kerjain_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
