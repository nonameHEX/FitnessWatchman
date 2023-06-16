package cz.mendelu.pef.fitnesswatchman.database.exercise

import androidx.room.*
import cz.mendelu.pef.fitnesswatchman.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    suspend fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE at_day_id = :dayId")
    fun getAllByDay(dayId: Long): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Long): Exercise

    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Delete
    suspend fun delete(exercise: Exercise)

    @Update
    suspend fun update(exercise: Exercise)

    @Query("UPDATE exercises SET exercise_state = :exerciseState WHERE id = :id")
    suspend fun changeState(id: Long, exerciseState: Boolean)

    @Query("DELETE FROM exercises WHERE at_day_id = :dayId")
    suspend fun deleteAllByDay(dayId: Long)
}