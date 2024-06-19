package com.example.selfiepal.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File


fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(email)
}
fun isValidUsername(username: String): Boolean {
    val usernameRegex = Regex("^[a-zA-Z0-9_]+$")
    return usernameRegex.matches(username)
}

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        else -> false
    }
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return filePath?.let { File(it) }
}

fun isMimeTypeVideo(context: Context, uri: Uri): Boolean {
    val mime = MimeTypeMap.getSingleton()
    val fileExtension = mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
    val mimeType = mime.getMimeTypeFromExtension(fileExtension)
    return mimeType?.contains("video") == true
}

