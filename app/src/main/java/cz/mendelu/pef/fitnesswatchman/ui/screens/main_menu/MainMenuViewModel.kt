package cz.mendelu.pef.fitnesswatchman.ui.screens.main_menu

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.fitnesswatchman.architecture.BaseViewModel
import cz.mendelu.pef.fitnesswatchman.database.day.IDayRepository
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val repository: IDayRepository
    ): BaseViewModel(){
    val mainMenuState: MutableState<MainMenuUIState> = mutableStateOf(MainMenuUIState.Default)

    fun loadDays(){
        launch {
            repository.getAll().collect{
                mainMenuState.value = MainMenuUIState.Success(it)
            }
        }
    }
}