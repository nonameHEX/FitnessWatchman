package cz.mendelu.pef.fitnesswatchman.database.day

import cz.mendelu.pef.fitnesswatchman.model.Day
import kotlinx.coroutines.flow.Flow

class DayRepositoryImpl(private val dayDao: DayDao): IDayRepository {
    override fun getAll(): Flow<List<Day>> {
        return dayDao.getAll()
    }

    override suspend fun getDayById(id: Long): Day {
        return dayDao.getDayById(id)
    }

    override suspend fun changeActivity(id: Long, activityId: Long) {
        dayDao.changeActivity(id, activityId)
    }
}