package cz.mendelu.pef.fitnesswatchman.database.day

import cz.mendelu.pef.fitnesswatchman.model.Day
import kotlinx.coroutines.flow.Flow

interface IDayRepository {
    fun getAll(): Flow<List<Day>>
    suspend fun getDayById(id: Long): Day
    suspend fun changeActivity(id: Long, activityId: Long)
}