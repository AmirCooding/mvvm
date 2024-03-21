package com.amircodeing.mvvm.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The purpose of the NoteApplication class, which serves as the entry point for the application.
 * The use of @HiltAndroidApp annotation, indicating that this application class is using Hilt for dependency injection.
 * The inheritance from Application, signifying that NoteApplication is an Android application class.
 */
@HiltAndroidApp
class NoteApplication : Application()

