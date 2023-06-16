package cz.mendelu.pef.fitnesswatchman.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.pef.fitnesswatchman.database.day.DayDao
import cz.mendelu.pef.fitnesswatchman.database.exercise.ExerciseDao
import cz.mendelu.pef.fitnesswatchman.model.Day
import cz.mendelu.pef.fitnesswatchman.model.Exercise

@Database(entities = [Exercise::class, Day::class], version = 1, exportSchema = true)
abstract class ExerciseDatabase: RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun dayDao(): DayDao

    companion object {
        private var INSTANCE: ExerciseDatabase? = null
        fun getDatabase(context: Context): ExerciseDatabase {
            if (INSTANCE == null) {
                synchronized(ExerciseDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ExerciseDatabase::class.java, "exercise_database"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}