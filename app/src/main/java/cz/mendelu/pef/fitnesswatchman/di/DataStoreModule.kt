package cz.mendelu.pef.fitnesswatchman.di

import android.content.Context
import cz.mendelu.pef.fitnesswatchman.datastore.DataStoreRepositoryImpl
import cz.mendelu.pef.fitnesswatchman.datastore.IDataStoreRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    fun provideDataStoreRepository(context: Context): IDataStoreRepository = DataStoreRepositoryImpl(context)

    single { provideDataStoreRepository(androidContext()) }
}

