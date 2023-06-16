package cz.mendelu.pef.fitnesswatchman.di

import cz.mendelu.pef.fitnesswatchman.database.exercise.ExerciseDao
import cz.mendelu.pef.fitnesswatchman.database.ExerciseDatabase
import cz.mendelu.pef.fitnesswatchman.database.day.DayDao
import org.koin.dsl.module

val daoModule = module {
    // Funkce pro instanci ExerciseDao
    fun provideExerciseDao(database: ExerciseDatabase): ExerciseDao = database.exerciseDao()

    // Funkce pro instanci DayDao
    fun provideDayDao(database: ExerciseDatabase): DayDao = database.dayDao()

    single { provideExerciseDao(get()) }
    single { provideDayDao(get()) }
}