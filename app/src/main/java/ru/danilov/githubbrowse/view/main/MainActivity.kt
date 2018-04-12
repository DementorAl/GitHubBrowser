package ru.danilov.githubbrowse.view.main

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import ru.danilov.githubbrowse.R
import ru.danilov.githubbrowse.presenter.MainPresenter
import ru.danilov.githubbrowse.view.peopleSearch.PeopleSearchFragment

class MainActivity : AppCompatActivity(), MainView,
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MAIN_FRAGMENT_TAG = ":main_fragment_tag:"
        const val LOGOUT_FRAGMENT_TAG = ":logout_fragment_tag:"
    }


    private val presenter: MainPresenter by lazy { MainPresenter(this) }
    private val mainFragment: PeopleSearchFragment = PeopleSearchFragment.newInstance()
    private val logoutFragment: LogoutFragment = LogoutFragment.newInstance()

    private val userNavPicView: CircleImageView by lazy { nav_view.getHeaderView(0).userPhotoView }
    private val userNavNameView: TextView by lazy { nav_view.getHeaderView(0).userNameView }
    private val userNavLoginView: TextView by lazy { nav_view.getHeaderView(0).userLoginView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        presenter.initState(savedInstanceState == null)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun initMenu() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun setUserName(name: String) {
        userNavNameView.text = name
    }

    override fun setEmptyUserName() {
        userNavNameView.text = getString(R.string.unknown)
    }

    override fun setUserLogin(login: String) {
        userNavLoginView.text = login
    }

    override fun setEmptyUserLogin() {
        userNavLoginView.text = getString(R.string.unknown)
    }

    override fun setUserAvatar(avatar: String) {
        Picasso.with(this).load(avatar)
                .placeholder(R.mipmap.ic_launcher_round).into(userNavPicView)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.people_item -> {
                showMainFragment()
            }
            R.id.logout_item -> {
                showLogoutFragment()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showMainFragment() {
        if (!mainFragment.isVisible) {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, mainFragment, MAIN_FRAGMENT_TAG)
            transaction.commit()
        }
    }

    override fun showLogoutFragment() {
        if (!logoutFragment.isVisible) {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, logoutFragment, LOGOUT_FRAGMENT_TAG)
            transaction.commit()
        }
    }


    object RxSchedulers {
        fun io(): Scheduler {
            return Schedulers.io()
        }

        fun main(): Scheduler {
            return AndroidSchedulers.mainThread()
        }
    }
}
