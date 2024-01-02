package ua.com.honchar.arstudy.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavBackStackEntry

inline fun <reified T : Parcelable> NavBackStackEntry.getParcelable(key: String): T? =
    arguments?.parcelable(key)

inline fun <reified T : Parcelable> Bundle?.parcelable(key: String): T? {
    return this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            it.getParcelable(key, T::class.java)
        } else {
            it.getParcelable(key)
        }
    }
}