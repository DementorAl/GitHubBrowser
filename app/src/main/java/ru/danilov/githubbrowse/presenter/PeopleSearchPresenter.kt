package ru.danilov.githubbrowse.presenter

import ru.danilov.githubbrowse.repository.NetworkRepository
import ru.danilov.githubbrowse.utils.async
import ru.danilov.githubbrowse.view.peopleSearch.GetNextPageCallback
import ru.danilov.githubbrowse.view.peopleSearch.PeopleSearchView

class PeopleSearchPresenter(private val view: PeopleSearchView) : GetNextPageCallback {

    private var page: Int = 1
    private var name: String = ""

    fun initState() {
        view.addSearchChangeListener()
        loadNextPage()
    }

    fun setSearchName(nameSearch: String) {
        if (name != nameSearch) {
            page = 1
            name = nameSearch
            loadNextPage()

        }
    }

    override fun loadNextPage() {
        val needClear = page == 1
        if (needClear) {
            view.showLoading()
        }
        NetworkRepository.service.getPeopleList("$name in:login type:user", page)
                .async()
                .subscribe({
                    view.addData(it.items, needClear)
                    if (needClear) {
                        view.hideLoading()
                    }
                    page++
                }, {
                    view.hideLoading()
                    view.showError(it.message ?: "")
                })
    }

}