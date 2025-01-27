package com.androngel.todolist

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todolist.dat"

fun writeData(items: SnapshotStateList<String>, context: Context) {
    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { fos ->
        val oas = ObjectOutputStream(fos)
        val itemList = ArrayList<String>()
        itemList.addAll(items)
        oas.writeObject(itemList)
    }
}

fun readData(context: Context): List<String> {
    try {
        context.openFileInput(FILE_NAME).use { fis ->
            val ois = ObjectInputStream(fis)
            return ois.readObject() as ArrayList<String>
        }
    } catch (e: FileNotFoundException) {
        return ArrayList()
    }
}