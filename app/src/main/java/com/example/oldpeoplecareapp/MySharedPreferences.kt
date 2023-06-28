package com.example.oldpeoplecareapp

import android.content.Context

class MySharedPreferences(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)

    fun getValue(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}
