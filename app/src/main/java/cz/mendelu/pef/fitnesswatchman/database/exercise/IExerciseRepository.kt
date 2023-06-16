package cz.mendelu.pef.fitnesswatchman.database.exercise

import cz.mendelu.pef.fitnesswatchman.model.Exercise
import kotlinx.coroutines.flow.Flow

interface IExerciseRepository {
    fun getAllByDay(dayId: Long): Flow<List<Exercise>>

    suspend fun getExerciseById(id: Long): Exercise

    suspend fun insert(exercise: Exercise): Long

    suspend fun delete(exercise: Exercise)

    suspend fun update(exercise: Exercise)

    suspend fun changeState(id: Long, exerciseState: Boolean)

    suspend fun deleteAllByDay(dayId: Long)
}