package ru.danilov.githubbrowse.view.login

interface LoginView {

    fun successAuthAction()

    fun initFBState()

    fun initVkState()

    fun initGoogleState()

    fun showError(message: String)
}