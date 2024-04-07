package com.amircodeing.mvvm.di.modules

import android.content.Context
import android.window.OnBackInvokedDispatcher
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.amircodeing.mvvm.di.scops.ApplicationScope
import com.amircodeing.mvvm.di.scops.IODispatcherScope
import com.amircodeing.mvvm.utils.Constants.PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideNoteDataStorePreferences(
        @ApplicationContext context: Context,
        @IODispatcherScope dispatcher: CoroutineDispatcher
    ) = PreferenceDataStoreFactory.create(
        scope = CoroutineScope(dispatcher + SupervisorJob()),
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ), produceFile = {
            context.preferencesDataStoreFile(PREFERENCES_NAME)
        }
    )
}
