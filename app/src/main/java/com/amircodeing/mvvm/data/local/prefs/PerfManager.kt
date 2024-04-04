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

enum class SortBy {
    DATE, NAME
}

/**
 * @DataStore provides a safe and durable way to store small amounts of data, such as preferences and application state.
 * DataStore bietet eine sichere und dauerhafte Möglichkeit, kleine Datenmengen wie Einstellungen und Anwendungsstatus zu speichern.
 * i do to combine die
 */
class PerfManager @Inject constructor(private val appDataStore: DataStore<Preferences>) {


    // Define a key for your preference
    private object PreferencesKey {
        val sort = stringPreferencesKey(PREFERENCES_SORT_KEY)
        val favorite = booleanPreferencesKey(PREFERENCES_FAVORITE_KEY)
    }

    /**
     * @Suspend Function  : Eine Funktion, die sich selbst anhält und zu gegebener Zeit zum Hauptstream zurückkehrt
     */

    // Suspend function to update the sort order in DataStore
    suspend fun saveSortOrder(sortBy: SortBy) = appDataStore.edit { preferences ->
        preferences[sort] = sortBy.name
    }

    suspend fun saveFavorite(favoriteState: Boolean) =
        appDataStore.edit { preferences -> preferences[favorite] = favoriteState }


    // read sort
    val readSortOrder = appDataStore.data.catch { exception ->
        if (exception is IOException) emit(
            emptyPreferences()
        ) else throw exception
    }.map { prferences -> prferences[sort] ?: SortBy.DATE.name }
    // read sort read isFavorite
    val readFavorite = appDataStore.data.catch { exception ->
        if (exception is IOException) emit(
            emptyPreferences()
        ) else throw exception
    }.map { preferences -> preferences[favorite] ?: FAVORITE }



// Here I took both together ( SortBy , iSFavorite ) and put them in one data class
    val readSearchNote = appDataStore.data.catch { exception ->
        if (exception is IOException) emit(
            emptyPreferences()
        ) else throw exception
    }.map { preferences ->
        FilterPrefs(
            SortBy.valueOf(preferences[sort] ?: SortBy.NAME.name), preferences[favorite] ?: FAVORITE
        )
    }
}

data class FilterPrefs(val sortBy: SortBy, val isFavorite: Boolean)