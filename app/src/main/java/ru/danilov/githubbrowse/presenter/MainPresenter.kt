package ru.danilov.githubbrowse.presenter

import ru.danilov.githubbrowse.repository.PreferenceRepository
import ru.danilov.githubbrowse.view.main.MainView

class MainPresenter(val view: MainView) {

    fun initState(f : Boolean) {
        view.initMenu()
        if (PreferenceRepository.name.isEmpty())
            view.setEmptyUserName()
        else
            view.setUserName(PreferenceRepository.name)

        if (PreferenceRepository.login.isEmpty())
            view.setEmptyUserLogin()
        else
            view.setUserLogin(PreferenceRepository.login)
        view.setUserAvatar(PreferenceRepository.avatar)
        if (f) {
            view.showMainFragment()
        }
    }


}