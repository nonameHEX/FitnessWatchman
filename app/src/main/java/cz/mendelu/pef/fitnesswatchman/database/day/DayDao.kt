package cz.mendelu.pef.fitnesswatchman.database.day

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.fitnesswatchman.model.Day
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {
    @Query("SELECT * FROM days")
    fun getAll(): Flow<List<Day>>

    @Query("SELECT * FROM days")
    suspend fun getAllDays(): List<Day>

    @Query("SELECT * FROM days WHERE id = :id")
    suspend fun getDayById(id: Long): Day

    @Insert
    suspend fun insert(day: Day)

    @Query("UPDATE days SET activity_id = :activityId WHERE id = :id")
    suspend fun changeActivity(id: Long, activityId: Long)

    @Update
    suspend fun update(day: Day)
}
