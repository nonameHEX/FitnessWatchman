package cz.mendelu.pef.fitnesswatchman.ui.screens.options

import cz.mendelu.pef.fitnesswatchman.model.Day

class OptionsScreenData {
    var userName: String = ""
    var rotateStatus: Boolean = false
    var dayList: List<Day> = mutableListOf(Day(""))
}