package ua.com.honchar.arstudy.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    fun downloadFile(fileUrl: String, fileName: String, context: Context, success: (filePath: String) -> Unit) {
        viewModelScope.launch {
            // Inside a CoroutineScope (e.g., in a ViewModel or in a CoroutineScope defined in your activity/fragment)
//            launch {
//
//                val file = downloadAndSaveFile(context, fileUrl, fileName)
//                success.invoke(file.toString())
//            }

            try {
                launch(Dispatchers.IO) {
                    val filePath = context.applicationContext.dataDir.absolutePath
                    val zipName = "${fileName.replace(".glb", "")}.zip"
                    val zipFilePath = "$filePath/$zipName"
                    val outputFileZip = File(zipFilePath)
                    val u = URL(fileUrl)
                    val conn = u.openConnection()
                    val contentLength = conn.contentLength
                    val stream = DataInputStream(u.openStream())
                    val buffer = ByteArray(contentLength)
                    stream.readFully(buffer)
                    stream.close()
                    val fos = DataOutputStream(FileOutputStream(outputFileZip))
                    fos.write(buffer)
                    fos.flush()
                    fos.close()
                    unzip(outputFileZip, filePath)
                    val unzippedFile = File("$filePath/$fileName")
                    val unzippedFilePath = Uri.fromFile(unzippedFile)
                    success.invoke(unzippedFilePath.toString())
                }
            } catch (e: FileNotFoundException) {
//                return  // swallow a 404
            } catch (e: IOException) {
//                return  // swallow a 404
            }
        }
    }

    suspend fun downloadAndSaveFile(context: Context, fileUrl: String, fileName: String): Uri? =
        withContext(Dispatchers.IO) {
            val filePath = context.applicationContext.dataDir.absolutePath
            val zipName = "${fileName.replace(".glb", "")}.zip"
            try {
                val url = URL(fileUrl)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                // Create a new file output stream
                val outputFile = File("$filePath/$zipName")
                val fileOutput = FileOutputStream(outputFile)

                // Stream to read file
                val inputStream = urlConnection.inputStream

                // Create buffer to read data in chunks
                val buffer = ByteArray(1024)
                var bufferLength: Int

                // Write data to file
                while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                    fileOutput.write(buffer, 0, bufferLength)
                }

                // Close the streams
                fileOutput.close()
                inputStream.close()

                unpackZip("$filePath/", zipName)

                val modelFile = File("$filePath/$fileName")
                val fileUri = Uri.fromFile(modelFile)

                fileUri
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: String) {

        File(destDirectory).run {
            if (!exists()) {
                mkdirs()
            }
        }

        ZipFile(zipFilePath).use { zip ->

            zip.entries().asSequence().forEach { entry ->

                zip.getInputStream(entry).use { input ->


                    val filePath = destDirectory + File.separator + entry.name

                    if (!entry.isDirectory) {
                        // if the entry is a file, extracts it
                        extractFile(input, filePath)
                    } else {
                        // if the entry is a directory, make the directory
                        val dir = File(filePath)
                        dir.mkdir()
                    }

                }

            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param inputStream
     * @param destFilePath
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(4096)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    /**
     * Size of the buffer to read/write data
     */
//    private const val BUFFER_SIZE = 4096


    private fun unpackZip(path: String, zipname: String): Boolean {
        val inS: InputStream
        val zis: ZipInputStream
        try {
            var filename: String
            inS = FileInputStream(path + zipname)
            zis = ZipInputStream(BufferedInputStream(inS))
            var ze: ZipEntry
            val buffer = ByteArray(1024)
            var count: Int
            while (zis.nextEntry != null) {
                ze = zis.nextEntry
                filename = ze.name

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory) {
                    val fmd = File(path + filename)
                    fmd.mkdirs()
                    continue
                }
                val fout = FileOutputStream(path + filename)
                while (zis.read(buffer).also { count = it } != -1) {
                    fout.write(buffer, 0, count)
                }
                fout.close()
                zis.closeEntry()
            }
            zis.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }
}