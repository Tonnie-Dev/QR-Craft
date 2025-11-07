package com.tonyxlab.qrcraft.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.tonyxlab.qrcraft.util.Constants.QR_CRAFT_DATASTORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = QR_CRAFT_DATASTORE)

class DataStore(context: Context) {

    val datastore = context.dataStore

    suspend fun setSnackbarShownStatus(isShown: Boolean) {
        datastore.edit { prefs ->
            prefs[CAMERA_SNACKBAR_SHOWN] = isShown
        }
    }
     fun getSnackbarShownStatus(): Flow<Boolean>  {

        return datastore.data.map { prefs -> prefs[CAMERA_SNACKBAR_SHOWN]?: false }
    }

    companion object {
        private val CAMERA_SNACKBAR_SHOWN = booleanPreferencesKey("cam_perm_status")
    }
}