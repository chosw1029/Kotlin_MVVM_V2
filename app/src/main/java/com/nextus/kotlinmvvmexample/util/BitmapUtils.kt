package com.nextus.kotlinmvvmexample.util

import android.graphics.Bitmap
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object BitmapUtils {

    fun resizeAndCompressImage(bitmap: Bitmap, file: File, size: String): File {
        // original measurements
        val origWidth = bitmap.width
        val origHeight = bitmap.height
        val imageSize = getSizeToMega(size)

        if(imageSize > 1) {
            resizeProcess(origWidth, origHeight, bitmap, file)
        } else {
            val outStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outStream)
            val fo = FileOutputStream(file)
            fo.write(outStream.toByteArray())
            fo.close()
        }

        return file
    }

    private fun resizeProcess(origWidth: Int, origHeight: Int, bitmap: Bitmap, file: File) {
        try {
            /*val destWidth = if (origWidth > origHeight) 3840 else 2160 //or the width you need

            if (origWidth > destWidth) {
                val destHeight = destWidth * origHeight / origWidth
                // we create an scaled bitmap so it reduces the image, not just trim it
                val b2 = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
                val outStream = ByteArrayOutputStream()
                // compress to the format you want, JPEG, PNG...
                // 70 is the 0-100 quality percentage
                b2.compress(Bitmap.CompressFormat.JPEG, 70, outStream)
                // we save the file, at least until we have made use of it
                // file.createNewFile()
                //write the bytes in file
                val fo = FileOutputStream(file)
                fo.write(outStream.toByteArray())
                // remember close de FileOutput
                fo.close()
            }*/
            // we create an scaled bitmap so it reduces the image, not just trim it
            val b2 = Bitmap.createScaledBitmap(bitmap, origWidth, origHeight, false)
            val outStream = ByteArrayOutputStream()
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.JPEG, 80, outStream)
            // we save the file, at least until we have made use of it
            // file.createNewFile()
            //write the bytes in file
            val fo = FileOutputStream(file)
            fo.write(outStream.toByteArray())
            // remember close de FileOutput
            fo.close()
            bitmap.recycle()
            b2.recycle()

        } catch (e: Exception) {
            Timber.e("compressBitmap: Error on saving file")
        }
    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
        inputStream.close()
    }

    fun getSizeToMega(size: String) = (size.toInt() / 1024) / 1024
}