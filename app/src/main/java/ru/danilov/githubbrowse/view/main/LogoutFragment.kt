package ru.danilov.githubbrowse.view.main

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_logout.*
import ru.danilov.githubbrowse.R
import ru.danilov.githubbrowse.repository.PreferenceRepository
import ru.danilov.githubbrowse.utils.onClick
import ru.danilov.githubbrowse.utils.startActivity
import ru.danilov.githubbrowse.view.login.LoginActivity

class LogoutFragment : Fragment() {

    companion object {
        fun newInstance(): LogoutFragment = LogoutFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutButton.onClick {
            PreferenceRepository.clear()
            activity.startActivity<LoginActivity>()
            activity.finish()
        }
    }
}

