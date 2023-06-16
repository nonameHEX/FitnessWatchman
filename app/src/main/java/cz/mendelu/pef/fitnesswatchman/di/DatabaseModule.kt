package cz.mendelu.pef.fitnesswatchman.di

import cz.mendelu.pef.fitnesswatchman.FitnessWatchmanApplication
import cz.mendelu.pef.fitnesswatchman.database.ExerciseDatabase
import org.koin.dsl.module


val databaseModule = module {
    fun provideDatabase(): ExerciseDatabase = ExerciseDatabase.getDatabase(FitnessWatchmanApplication.appContext)

    single { provideDatabase() }
}