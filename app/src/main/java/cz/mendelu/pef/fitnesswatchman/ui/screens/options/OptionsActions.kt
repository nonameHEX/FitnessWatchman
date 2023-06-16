package cz.mendelu.pef.fitnesswatchman.ui.screens.options

interface OptionsActions {
    fun onNameChange(name: String)
    fun onNameClear()
    fun changeRotateStatus(state: Boolean)
    fun deleteAllPhotos()
    fun resetAllSchedule()
}