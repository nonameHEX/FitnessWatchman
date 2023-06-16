package cz.mendelu.pef.fitnesswatchman.ui.screens.options

sealed class OptionsUIState {
    object Default: OptionsUIState()
    object OptionsLoading: OptionsUIState()
    object OptionsChanged: OptionsUIState()
}