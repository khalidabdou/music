package com.anafthdev.musicompose2.utils


import android.content.Context
import android.os.Environment
import com.anafthdev.musicompose2.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class Download {


    fun start(context: Context){

        var `in`: InputStream? = null
        var fout: FileOutputStream? = null
        try {
            `in` = context.resources.openRawResource(R.raw.alarm)
            val downloadsDirectoryPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

            val filename="myFile.mp3"
            fout= FileOutputStream(File(downloadsDirectoryPath+filename))
            val data = ByteArray(1024)
            var count: Int
            while (`in`.read(data, 0, 1024).also { count = it } != -1) {
                fout.write(data, 0, count)
            }
        }finally {
            `in`?.close()
            if (fout != null) {
                fout.close();
            }

        }


    }


}