package ru.danilov.githubbrowse

import android.app.Application
import android.content.ContextWrapper
import com.facebook.FacebookSdk
import com.pixplicity.easyprefs.library.Prefs
import com.vk.sdk.VKSdk

class GitHubBrowseApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setUseDefaultSharedPreference(true).build()
        VKSdk.initialize(applicationContext)
    }
}