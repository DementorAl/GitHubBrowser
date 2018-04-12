package ru.danilov.githubbrowse.repository

import com.facebook.login.LoginManager
import com.pixplicity.easyprefs.library.Prefs

object PreferenceRepository {
    fun clear() {
        LoginManager.getInstance().logOut()
        Prefs.clear()
    }

    private const val KEY_TOKEN = "token"
    private const val KEY_NAME = "user_name"
    private const val KEY_LOGIN = "user_login"
    private const val KEY_AVATAR = "user_avatar"

    var token: String
        get() = Prefs.getString(KEY_TOKEN, "")
        set(value) = Prefs.putString(KEY_TOKEN, value)

    var name: String
        get() = Prefs.getString(KEY_NAME, "")
        set(value) = Prefs.putString(KEY_NAME, value)

    var login: String
        get() = Prefs.getString(KEY_LOGIN, "")
        set(value) = Prefs.putString(KEY_LOGIN, value)

    var avatar: String
        get() = Prefs.getString(KEY_AVATAR, "")
        set(value) = Prefs.putString(KEY_AVATAR, value)
}