package com.amircodeing.mvvm.data.local.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amircodeing.mvvm.data.local.prefs.PerfManager.PreferencesKey.favorite
import com.amircodeing.mvvm.data.local.prefs.PerfManager.PreferencesKey.sort
import com.amircodeing.mvvm.utils.Constants.FAVORITE
import com.amircodeing.mvvm.utils.Constants.PREFERENCES_FAVORITE_KEY
import com.amircodeing.mvvm.utils.Constants.PREFERENCES_SORT_KEY
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


/**
 * @DataStore provides a safe and durable way to store small amounts of data, such as preferences and application state.
 * DataStore bietet eine sichere und dauerhafte Möglichkeit, kleine Datenmengen wie Einstellungen und Anwendungsstatus zu speichern.
 *
 */
class PerfManager @Inject constructor(private val appDataStore: DataStore<Preferences>) {

// Assume appDataStore is your DataStore<Preferences> instance

    // Define a key for your preference
    private object PreferencesKey {
        val sort = stringPreferencesKey(PREFERENCES_SORT_KEY)
        val favorite = booleanPreferencesKey(PREFERENCES_FAVORITE_KEY)
    }

    // Suspend function to update the sort order in DataStore
    suspend fun saveSortOrder(sortBy: SortBy) = appDataStore.edit { preferences ->
        preferences[sort] = sortBy.name
    }

    val readSortOrder = appDataStore.data.catch { exception ->
        if (exception is IOException) emit(
            emptyPreferences()
        ) else throw exception
    }.map { prferences -> prferences[sort] ?: SortBy.DATE.name }

    suspend fun saveFavorite(favoriteState: Boolean) =
        appDataStore.edit { preferences -> preferences[favorite] = favoriteState }

    val readFavorite = appDataStore.data.catch { exception ->
        if (exception is IOException) emit(
            emptyPreferences()
        ) else throw exception
    }.map { preferences -> preferences[favorite] ?: FAVORITE }
}

enum class SortBy {
    NAME, DATE,
}
