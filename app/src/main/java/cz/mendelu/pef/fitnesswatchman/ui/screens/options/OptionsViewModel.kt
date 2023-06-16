package cz.mendelu.pef.fitnesswatchman.ui.screens.options

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.fitnesswatchman.architecture.BaseViewModel
import cz.mendelu.pef.fitnesswatchman.database.day.IDayRepository
import cz.mendelu.pef.fitnesswatchman.database.exercise.IExerciseRepository
import cz.mendelu.pef.fitnesswatchman.datastore.IDataStoreRepository
import cz.mendelu.pef.fitnesswatchman.utils.DatabaseUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val exerciseRepository: IExerciseRepository,
    private val dayRepository: IDayRepository,
    private val dataStore: IDataStoreRepository
    ): BaseViewModel(), OptionsActions{
    val optionsUIState: MutableState<OptionsUIState> = mutableStateOf(OptionsUIState.OptionsLoading)

    var data = OptionsScreenData()

    fun loadOptions(){
        launch {
            println(dataStore.userNameFlow.first())
            data.userName = dataStore.userNameFlow.first()
            data.rotateStatus = dataStore.rotateExercisesStatusFlow.first()
            dayRepository.getAll().collect(){
                data.dayList = it
                optionsUIState.value = OptionsUIState.Default
            }
        }
    }

    fun saveOptions(){
        launch {
            dataStore.saveUserName(data.userName)
            dataStore.saveRotateExercisesStatus(data.rotateStatus)
        }
    }
    override fun onNameChange(name: String) {
        data.userName = name
        optionsUIState.value = OptionsUIState.OptionsChanged
    }

    override fun onNameClear() {
        data.userName = ""
        optionsUIState.value = OptionsUIState.OptionsChanged
    }

    override fun changeRotateStatus(state: Boolean) {
        launch {
            data.rotateStatus = state
            dataStore.saveRotateExercisesStatus(data.rotateStatus)
            optionsUIState.value = OptionsUIState.OptionsChanged
        }
    }

    override fun deleteAllPhotos() {
        launch {
            dataStore.deleteAllPhotos()
        }
    }

    override fun resetAllSchedule() {
        launch {
            for (day in data.dayList){
                exerciseRepository.deleteAllByDay(day.id!!)
                dayRepository.changeActivity(day.id!!,DatabaseUtils.getDefaultFocusPartId())
            }
        }
    }
}