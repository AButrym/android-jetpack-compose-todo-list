package com.androngel.todolist

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todolist.dat"

fun writeData(items: List<String>, context: Context) {
    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
        ObjectOutputStream(it).writeObject(
            ArrayList<String>(items)
        )
    }
}

fun readData(context: Context): List<String> {
    try {
        @Suppress("UNCHECKED_CAST")
        return context.openFileInput(FILE_NAME).use {
            ObjectInputStream(it).readObject()
        } as ArrayList<String>
    } catch (e: Exception) {
        return ArrayList()
    }
}