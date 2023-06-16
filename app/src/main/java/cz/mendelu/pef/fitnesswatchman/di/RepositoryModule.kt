package cz.mendelu.pef.fitnesswatchman.di

import cz.mendelu.pef.fitnesswatchman.database.day.DayDao
import cz.mendelu.pef.fitnesswatchman.database.day.DayRepositoryImpl
import cz.mendelu.pef.fitnesswatchman.database.day.IDayRepository
import cz.mendelu.pef.fitnesswatchman.database.exercise.ExerciseDao
import cz.mendelu.pef.fitnesswatchman.database.exercise.ExerciseRepositoryImpl
import cz.mendelu.pef.fitnesswatchman.database.exercise.IExerciseRepository
import org.koin.dsl.module

val repositoryModule = module {
    // Funkce pro instanci ExerciseDay repozitáře
    fun provideExerciseRepository(dao: ExerciseDao): IExerciseRepository {
        return ExerciseRepositoryImpl(dao)
    }

    // Funkce pro instanci DayDao repozitáře
    fun provideDayRepository(dao: DayDao): IDayRepository {
        return DayRepositoryImpl(dao)
    }

    single { provideExerciseRepository(get()) }
    single { provideDayRepository(get()) }
}