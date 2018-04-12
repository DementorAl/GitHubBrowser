package ru.danilov.githubbrowse.view.peopleSearch

import ru.danilov.githubbrowse.model.PeopleSearchItemModel

interface PeopleSearchView{

    fun addData(data : List<PeopleSearchItemModel>,needClear : Boolean)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun addSearchChangeListener()

}