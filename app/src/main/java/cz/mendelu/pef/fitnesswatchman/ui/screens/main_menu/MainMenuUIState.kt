package cz.mendelu.pef.fitnesswatchman.ui.screens.main_menu

import cz.mendelu.pef.fitnesswatchman.model.Day

sealed class MainMenuUIState {
    object Default : MainMenuUIState()
    class Success(val days: List<Day>): MainMenuUIState()
}