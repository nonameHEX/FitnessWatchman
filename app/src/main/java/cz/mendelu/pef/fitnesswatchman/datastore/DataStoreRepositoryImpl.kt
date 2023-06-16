package cz.mendelu.pef.fitnesswatchman.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(private val context: Context): IDataStoreRepository{

    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val USER_NAME_KEY: Preferences.Key<String> =
            stringPreferencesKey(DataStoreConstants.USER_NAME)
        val ROTATE_EXERCISES_STATUS_KEY: Preferences.Key<Boolean> =
            booleanPreferencesKey(DataStoreConstants.ROTATE_EXERCISES_STATUS)
        val LAST_SAVED_TIMESTAMP_KEY: Preferences.Key<Long> =
            longPreferencesKey(DataStoreConstants.LAST_SAVED_TIMESTAMP)
        val SELECTED_PHOTOS_KEY: Preferences.Key<Set<String>> =
            stringSetPreferencesKey(DataStoreConstants.SELECTED_PHOTOS)
    }

     override val userNameFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY] ?: ""
        }
    override val rotateExercisesStatusFlow: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[ROTATE_EXERCISES_STATUS_KEY] ?: false
        }
    override val lastSavedTimestampFlow: Flow<Long>
        get() = dataStore.data.map { preferences ->
            preferences[LAST_SAVED_TIMESTAMP_KEY] ?: 0L
        }
    override val selectedPhotosFlow: Flow<Set<String>>
        get() = dataStore.data.map { preferences ->
            preferences[SELECTED_PHOTOS_KEY] ?: emptySet()
        }

    override suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = userName
        }
    }
    override suspend fun saveRotateExercisesStatus(rotateStatus: Boolean) {
        dataStore.edit { preferences ->
            preferences[ROTATE_EXERCISES_STATUS_KEY] = rotateStatus
        }
    }
    override suspend fun saveLastSavedTimestamp(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SAVED_TIMESTAMP_KEY] = timestamp
        }
    }
    override suspend fun saveSelectedPhotos(photoNames: Set<String>) {
        dataStore.edit { preferences ->
            preferences[SELECTED_PHOTOS_KEY] = photoNames
        }
    }
    override suspend fun deleteAllPhotos() {
        dataStore.edit { preferences ->
            preferences.remove(SELECTED_PHOTOS_KEY)
        }
    }
}