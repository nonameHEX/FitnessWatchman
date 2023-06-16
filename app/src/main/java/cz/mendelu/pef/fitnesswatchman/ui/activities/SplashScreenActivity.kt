package cz.mendelu.pef.fitnesswatchman.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cz.mendelu.pef.fitnesswatchman.R
import cz.mendelu.pef.fitnesswatchman.database.ExerciseDatabase
import cz.mendelu.pef.fitnesswatchman.datastore.DataStoreRepositoryImpl
import cz.mendelu.pef.fitnesswatchman.model.Day
import cz.mendelu.pef.fitnesswatchman.utils.LanguageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var isDatabaseInitialized = mutableStateOf(false)
    private val welcomeUser = mutableStateOf("")
    private var transitionsCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        val dataStoreRepository = DataStoreRepositoryImpl(applicationContext)
        val splashScreen = installSplashScreen()


        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.userNameFlow.collect { userName ->
                welcomeUser.value = userName
            }
        }

        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        continueToMainActivity()
    }

    private fun continueToMainActivity() {
        val dataStoreRepository = DataStoreRepositoryImpl(applicationContext)
        initializeDatabaseIfNeeded()
        checkLanguageChange()

        CoroutineScope(Dispatchers.Main).launch {
            val rotateStatus = dataStoreRepository.rotateExercisesStatusFlow.first()
            val passedWeek = checkPassedWeek()

            if (rotateStatus && passedWeek) {
                rotateExercisesAndActivities()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            startActivity(MainActivity.createIntent(this@SplashScreenActivity))
            finish()
        }
    }

    private fun initializeDatabaseIfNeeded() {
        val dayDao = ExerciseDatabase.getDatabase(applicationContext).dayDao()
        if (!isDatabaseInitialized.value) {
            CoroutineScope(Dispatchers.IO).launch {
                val days = dayDao.getAll().firstOrNull()

                if (days == null || days.isEmpty()) {
                    val defaultDays = arrayOf(
                        getString(R.string.monday),
                        getString(R.string.tuesday),
                        getString(R.string.wednesday),
                        getString(R.string.thursday),
                        getString(R.string.friday),
                        getString(R.string.saturday),
                        getString(R.string.sunday)
                    )

                    for (dayName in defaultDays) {
                        val day = Day(dayName)
                        dayDao.insert(day)
                    }
                }

                isDatabaseInitialized.value = true
            }
        }
    }

    private fun checkLanguageChange() {
        val savedLanguage = LanguageUtils.getSavedLanguage(this)
        val systemLanguage = Locale.getDefault().language

        if (systemLanguage != savedLanguage) {
            LanguageUtils.saveLanguage(this, systemLanguage)
            updateDaysLanguage()
        }
    }

    private fun updateDaysLanguage() {
        val dayDao = ExerciseDatabase.getDatabase(applicationContext).dayDao()
        CoroutineScope(Dispatchers.IO).launch {
            val days = dayDao.getAll().firstOrNull()

            if (days != null && days.isNotEmpty()) {
                val defaultDays = arrayOf(
                    getString(R.string.monday),
                    getString(R.string.tuesday),
                    getString(R.string.wednesday),
                    getString(R.string.thursday),
                    getString(R.string.friday),
                    getString(R.string.saturday),
                    getString(R.string.sunday)
                )

                for (i in days.indices) {
                    val day = days[i]
                    day.name = defaultDays[i]
                    dayDao.update(day)
                }
            }
        }
    }

    private suspend fun checkPassedWeek(): Boolean {
        val dataStoreRepository = DataStoreRepositoryImpl(applicationContext)
        val lastRunTimestamp = dataStoreRepository.lastSavedTimestampFlow.first()
        val currentTimestamp = System.currentTimeMillis()

        val lastRunDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(lastRunTimestamp),
            ZoneId.systemDefault()
        )
        val currentDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(currentTimestamp),
            ZoneId.systemDefault()
        )

        val daysPassed = ChronoUnit.DAYS.between(lastRunDateTime, currentDateTime)

        transitionsCount = countSundayToMondayTransitions(lastRunDateTime, currentDateTime)

        val isSundayToMondayTransition = lastRunDateTime.dayOfWeek == DayOfWeek.SUNDAY &&
                currentDateTime.dayOfWeek == DayOfWeek.MONDAY

        val passedWeek = daysPassed >= 7 || isSundayToMondayTransition

        if (passedWeek) {
            dataStoreRepository.saveLastSavedTimestamp(currentTimestamp)
        }
        return passedWeek
    }

    private fun countSundayToMondayTransitions(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int {
        var transitions = 0
        var currentDate = startDate

        while (currentDate.isBefore(endDate)) {
            if (currentDate.dayOfWeek == DayOfWeek.SUNDAY && currentDate.plusDays(1).dayOfWeek == DayOfWeek.MONDAY) {
                transitions++
            }
            currentDate = currentDate.plusDays(1)
        }

        return transitions
    }

    private suspend fun rotateExercisesAndActivities() {
        val dayDao = ExerciseDatabase.getDatabase(applicationContext).dayDao()
        val exerciseDao = ExerciseDatabase.getDatabase(applicationContext).exerciseDao()

        for(i in 0 until  transitionsCount){
            val days = dayDao.getAllDays()
            val daysCopy = mutableListOf<Day>()
            val exercises = exerciseDao.getAll()

            val validDayIds = days.map { it.id }

            for (exercise in exercises) {
                val currentDayId = exercise.atDayId?.toInt()
                if (currentDayId != null) {
                    if (currentDayId == 7){
                        val newDayId = 1
                        if (validDayIds.contains(newDayId.toLong())) {
                            exercise.atDayId = newDayId.toLong()
                            exerciseDao.update(exercise)
                        }
                    }else{
                        val newDayId = currentDayId + 1
                        if (validDayIds.contains(newDayId.toLong())) {
                            exercise.atDayId = newDayId.toLong()
                            exerciseDao.update(exercise)
                        }
                    }
                }
            }

            for (day in days) {
                val copyDay = Day(day.name)
                copyDay.id = day.id
                copyDay.activityId = day.activityId
                daysCopy.add(copyDay)
            }

            for (index in days.indices) {
                val currentDay = days[index]
                val nextDay = daysCopy[(index + 1) % days.size]

                nextDay.activityId = currentDay.activityId
                dayDao.update(nextDay)
            }
        }
    }
}
