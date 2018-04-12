package ru.danilov.githubbrowse.view.main

interface MainView {
    fun setUserName(name: String)
    fun setEmptyUserName()
    fun setUserLogin(login: String)
    fun setEmptyUserLogin()
    fun setUserAvatar(avatar: String)
    fun initMenu()
    fun showMainFragment()
    fun showLogoutFragment()
}