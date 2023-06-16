package cz.mendelu.pef.fitnesswatchman.database.exercise

import cz.mendelu.pef.fitnesswatchman.model.Exercise
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(private val exerciseDao: ExerciseDao): IExerciseRepository {
    override fun getAllByDay(dayId: Long): Flow<List<Exercise>> {
        return exerciseDao.getAllByDay(dayId)
    }

    override suspend fun getExerciseById(id: Long): Exercise {
        return exerciseDao.getExerciseById(id)
    }

    override suspend fun insert(exercise: Exercise): Long {
        return exerciseDao.insert(exercise)
    }

    override suspend fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    override suspend fun update(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    override suspend fun changeState(id: Long, exerciseState: Boolean) {
        exerciseDao.changeState(id, exerciseState)
    }

    override suspend fun deleteAllByDay(dayId: Long) {
        exerciseDao.deleteAllByDay(dayId)
    }

}