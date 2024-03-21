package com.amircodeing.mvvm.di.scops

import androidx.room.Query
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope {
}