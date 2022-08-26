package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.database.schedule.ScheduleDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

//need to tell Room what to do with all of these classes

@Database(entities = arrayOf(Schedule::class), version = 1)

//use the Elvis operator to either return the existing instance of the database
//(if it already exists) or create the database for the first time if needed

abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("database/bus_schedule.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}