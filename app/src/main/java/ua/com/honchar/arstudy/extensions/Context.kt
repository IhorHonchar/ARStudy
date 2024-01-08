package ua.com.honchar.arstudy.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import kotlinx.coroutines.coroutineScope
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URL

@Throws(FileNotFoundException::class)
suspend fun Context.downloadFile(fileName: String, fileUrlPath: String): String {
    return coroutineScope {
        val filePath = applicationContext.dataDir.absolutePath
        val outputFile = File("$filePath/$fileName")
        val u = URL(fileUrlPath)
        val conn = u.openConnection()
        val contentLength = conn.contentLength
        val stream = DataInputStream(u.openStream())
        val buffer = ByteArray(contentLength)
        stream.readFully(buffer)
        stream.close()
        val fos = DataOutputStream(FileOutputStream(outputFile))
        fos.write(buffer)
        fos.flush()
        fos.close()
        val downloadedFileUri = Uri.fromFile(outputFile)
        downloadedFileUri.toString()
    }
}

@Throws(FileNotFoundException::class)
suspend fun Context.checkFileIsDownloaded(fileName: String): String? {
    return coroutineScope {
        val filePath = applicationContext.dataDir.absolutePath
        val outputFile = File("$filePath/${fileName}")
        if (outputFile.exists()) {
            Uri.fromFile(outputFile)?.toString()
        } else {
            throw FileNotFoundException("file not found")
        }
    }
}

fun Context.findActivity() : Activity? = when(this){
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

