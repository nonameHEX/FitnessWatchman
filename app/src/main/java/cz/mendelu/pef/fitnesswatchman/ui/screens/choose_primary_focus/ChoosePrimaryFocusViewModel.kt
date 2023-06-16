package cz.mendelu.pef.fitnesswatchman.ui.screens.choose_primary_focus

import cz.mendelu.pef.fitnesswatchman.architecture.BaseViewModel
import cz.mendelu.pef.fitnesswatchman.database.day.IDayRepository
import kotlinx.coroutines.launch

class ChoosePrimaryFocusViewModel(
    private val repository: IDayRepository
    ): BaseViewModel(), ChoosePrimaryFocusActions{
    override fun changePrimaryFocus(id: Long, partId: Long){
        launch {
            repository.changeActivity(id, partId)
        }
    }
}