package cz.mendelu.pef.fitnesswatchman.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToDaySchedule(id: Long)
    fun navigateToChoosePrimaryFocus(id: Long)
    fun returnFromChoosePrimaryFocus(activityChanged: Boolean)
    fun navigateToAddEditExercise(dayId: Long, id: Long?)
    fun navigateToOptions()
    fun navigateToPhotoGallery()
}