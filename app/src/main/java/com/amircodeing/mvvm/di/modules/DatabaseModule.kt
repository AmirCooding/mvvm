package com.amircodeing.mvvm.di.modules

import android.app.Application
import androidx.room.Room
import com.amircodeing.mvvm.data.local.db.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
// i need just one Instance from this one,that#s way i use SingletonComponent::class in @InstallIn annotation
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): NoteDatabase =
      Room.databaseBuilder(application,NoteDatabase::class.java, "mvvm_database").build()
}