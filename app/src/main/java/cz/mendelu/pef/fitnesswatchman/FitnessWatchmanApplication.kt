package cz.mendelu.pef.fitnesswatchman

import android.app.Application
import android.content.Context
import cz.mendelu.pef.fitnesswatchman.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FitnessWatchmanApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidContext(this@FitnessWatchmanApplication)
            modules(listOf(
                viewModelModule,
                repositoryModule,
                daoModule,
                databaseModule,
                dataStoreModule
            ))
        }
    }
    companion object {
        lateinit var appContext: Context
            private set
    }
}