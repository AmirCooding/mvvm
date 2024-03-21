package com.amircodeing.mvvm.di.modules

import android.app.Application
import androidx.room.Room
import com.amircodeing.mvvm.data.local.dataSource.LocalDataSource
import com.amircodeing.mvvm.data.local.dataSource.LocalDataSourceImpl
import com.amircodeing.mvvm.data.local.dao.NoteDao
import com.amircodeing.mvvm.data.local.db.NoteDatabase
import com.amircodeing.mvvm.data.repository.NoteRepository
import com.amircodeing.mvvm.di.scops.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
// i need just one Instance from this one,that's way i use SingletonComponent::class in @InstallIn annotation
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    // The application provides Context for Room to create the database
    fun provideDatabase(application: Application, callBack: NoteDatabase.CallBack): NoteDatabase =
        Room.databaseBuilder(application, NoteDatabase::class.java, "mvvm_database").allowMainThreadQueries()
            .addCallback(callBack)
            .build()

    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase) = noteDatabase.noteDao()

    @Provides
    fun provideLocalDataSource(notDao: NoteDao) = LocalDataSourceImpl(notDao)

    @Provides
    fun provideRepository(localDataSource: LocalDataSourceImpl) = NoteRepository(localDataSource)

    @Singleton
    @Provides
    @ApplicationScope
    fun provideApplication() = CoroutineScope(SupervisorJob())
}