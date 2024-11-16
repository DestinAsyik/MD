package com.id.destinasyik.data.local

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val PREF_NAME = "MyPrefs"
    private val TOKEN_KEY = "token_key"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Menyimpan token
    fun saveToken(token: String) {
        editor.putString(TOKEN_KEY, token)
        editor.apply() // gunakan apply() untuk menyimpan secara asinkron
    }

    // Mengambil token
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null) // null adalah nilai default jika token tidak ditemukan
    }

    // Menghapus token
    fun deleteToken() {
        editor.remove(TOKEN_KEY)
        editor.apply() // gunakan apply() untuk menyimpan secara asinkron
    }
}