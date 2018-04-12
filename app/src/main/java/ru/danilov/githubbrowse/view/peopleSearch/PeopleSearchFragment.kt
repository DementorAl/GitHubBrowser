package ru.danilov.githubbrowse.view.peopleSearch

import android.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import ru.danilov.githubbrowse.R
import ru.danilov.githubbrowse.model.PeopleSearchItemModel
import ru.danilov.githubbrowse.presenter.PeopleSearchPresenter
import ru.danilov.githubbrowse.utils.find
import ru.danilov.githubbrowse.utils.gone
import ru.danilov.githubbrowse.utils.visible


class PeopleSearchFragment : Fragment(), PeopleSearchView {

    companion object {
        fun newInstance(): PeopleSearchFragment = PeopleSearchFragment()
    }

    private val presenter: PeopleSearchPresenter by lazy { PeopleSearchPresenter(this) }
    private val adapter: PeopleSearchAdapter by lazy { PeopleSearchAdapter(presenter) }
    private var mData: String = ""
    lateinit var searchInputLayout: TextInputLayout
    lateinit var peopleRecyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    override fun addData(data: List<PeopleSearchItemModel>, needClear: Boolean) {
        if (needClear) {
            adapter.clearData()
        }
        adapter.refillData(ArrayList(data))
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_people_search, container, false)
    }

    //Здесь стоило сделать кеширование полученных записей, но для этого нужна DB
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("text", mData)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            mData = savedInstanceState.getString("text")
            searchInputLayout.editText?.setText(mData)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchInputLayout = view.find(R.id.searchInputLayout)
        peopleRecyclerView = view.find(R.id.peopleRecyclerView)
        progressBar = view.find(R.id.progressBar)
        peopleRecyclerView.layoutManager = LinearLayoutManager(activity)
        peopleRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        presenter.initState()
    }

    override fun addSearchChangeListener() {
        searchInputLayout.editText?.setSingleLine()
        searchInputLayout.editText?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchInputLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                object : CountDownTimer(600, 100) {
                    override fun onFinish() {
                        mData = searchInputLayout.editText?.text.toString()
                        presenter.setSearchName(searchInputLayout.editText?.text.toString())
                    }

                    override fun onTick(millisUntilFinished: Long) {
                    }
                }.start()
                true
            } else {
                false
            }
        }
    }

    override fun showLoading() {
        searchInputLayout.gone()
        peopleRecyclerView.gone()
        progressBar.visible()
    }

    override fun hideLoading() {
        searchInputLayout.visible()
        peopleRecyclerView.visible()
        progressBar.gone()
    }

    override fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}




