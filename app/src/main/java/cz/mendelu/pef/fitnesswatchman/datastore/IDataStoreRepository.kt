package cz.mendelu.pef.fitnesswatchman.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    val userNameFlow: Flow<String>
    val rotateExercisesStatusFlow: Flow<Boolean>
    val lastSavedTimestampFlow: Flow<Long>
    val selectedPhotosFlow: Flow<Set<String>>
    suspend fun saveUserName(userName: String)
    suspend fun saveRotateExercisesStatus(rotateStatus: Boolean)
    suspend fun saveLastSavedTimestamp(timestamp: Long)
    suspend fun saveSelectedPhotos(photoNames: Set<String>)
    suspend fun deleteAllPhotos()
}