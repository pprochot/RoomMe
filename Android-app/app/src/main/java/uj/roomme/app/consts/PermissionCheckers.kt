package uj.roomme.app.consts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission

object PermissionCheckers {

    fun isReadExternalStorageGranted(context: Context): Boolean {
        return isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun isGranted(context: Context, permission: String): Boolean {
        return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}