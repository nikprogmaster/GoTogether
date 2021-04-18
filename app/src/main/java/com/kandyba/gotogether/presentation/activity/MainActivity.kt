package com.kandyba.gotogether.presentation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.presentation.fragment.main.EventFragment
import com.kandyba.gotogether.presentation.fragment.main.ForYouFragment
import com.kandyba.gotogether.presentation.fragment.main.ProfileFragment
import com.kandyba.gotogether.presentation.fragment.main.SearchFragment
import com.kandyba.gotogether.presentation.viewmodel.EventDetailsViewModel
import com.kandyba.gotogether.presentation.viewmodel.ForYouViewModel
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.kandyba.gotogether.presentation.viewmodel.SearchViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var logoutButton: ImageView
    private lateinit var cancelAlertButton: Button
    private lateinit var confirmAlertButton: Button
    private lateinit var alertDialog: AlertDialog
    private lateinit var progress: LinearLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var appbarTitle: TextView
    private lateinit var appbarSubtitle: TextView
    private lateinit var backButton: ImageView
    private lateinit var navigation: BottomNavigationView

    private lateinit var forYouViewModel: ForYouViewModel
    private lateinit var eventDetailsViewModel: EventDetailsViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resolveDependencies()
        initViews()
        initListeners()
        initObservers()
        setAppbarTitle(getString(R.string.special_for_you))
        openFragment(ForYouFragment.newInstance())
    }

    private fun resolveDependencies() {
        //init di
        val component = (application as App).appComponent
        settings = component.getSharedPreferences()
        val forYouViewModelFactory = component.getForYouViewModelFactory()
        forYouViewModel = ViewModelProvider(this, forYouViewModelFactory)
            .get(ForYouViewModel::class.java)
        val eventDetailsViewModelFactory =
            component.getEventDetailsViewModelFactory()
        eventDetailsViewModel = ViewModelProvider(this, eventDetailsViewModelFactory)
            .get(EventDetailsViewModel::class.java)
        val searchViewModelFactory = component.getSearchViewModelFactory()
        searchViewModel = ViewModelProvider(this, searchViewModelFactory)
            .get(SearchViewModel::class.java)
        val mainViewModelFactory = component.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)
            .get(MainViewModel::class.java)

    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        progress = findViewById(R.id.progress)
        appbarTitle = findViewById(R.id.appbar_title)
        appbarSubtitle = findViewById(R.id.appbar_subtitle)
        backButton = findViewById(R.id.main_back_btn)
        navigation = findViewById(R.id.navigation)
        navigation.selectedItemId = R.id.for_you

        //init alert
        logoutButton = findViewById(R.id.exit)
        mainLayout = findViewById(R.id.main_layout)
        val alert = layoutInflater.inflate(R.layout.exit_dialog_layout, mainLayout, false)
        cancelAlertButton = alert.findViewById(R.id.cancel_btn)
        confirmAlertButton = alert.findViewById(R.id.logout_btn)
        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setView(alert)
        alertDialog = builder.create()
    }

    private fun initListeners() {
        logoutButton.setOnClickListener {
            cancelAlertButton.setOnClickListener { alertDialog.dismiss() }
            confirmAlertButton.setOnClickListener {
                forYouViewModel.logout(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
            }
            alertDialog.show()
        }
        backButton.setOnClickListener {
            closeFragment()
        }
        navigation.setOnNavigationItemSelectedListener {
            clearBackStack()
            when (it.itemId) {
                R.id.search -> {
                    showToolbar(false)
                    openFragment(SearchFragment.newInstance())
                    true
                }
                R.id.for_you -> {
                    openFragment(ForYouFragment.newInstance())
                    true
                }
                R.id.profile -> {
                    openFragment(ProfileFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
    }

    private fun clearBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun initObservers() {
        forYouViewModel.showProgress.observe(this, Observer { show -> showProgress(show) })
        forYouViewModel.showSnackbar.observe(this, Observer { mes -> showSnackbar(mes.message) })
        forYouViewModel.logoutCompleted.observe(this, Observer {
            showProgress(true)
            clearPrefs()
            openStartActivity()
        })
        searchViewModel.showProgress.observe(this, Observer { showProgress(it) })
        searchViewModel.showSnackbar.observe(this, Observer { mes -> showSnackbar(mes.message) })
        searchViewModel.eventInfo.observe(this, Observer {
            openFragment(EventFragment.newInstance(it))
        })
        mainViewModel.appbarTitle.observe(this, Observer { setAppbarTitle(it) })
        mainViewModel.closeFragment.observe(this, Observer { closeFragment() })
        mainViewModel.makeForYouToolbar.observe(this, Observer { makeForYouEventsToolbar() })
        mainViewModel.makeParticipantsToolbar.observe(this, Observer { makeParticipantsToolbar() })
        mainViewModel.openDialogFragment.observe(this, Observer { showDialogFragment(it) })
        mainViewModel.openFragment.observe(this, Observer { openFragment(it) })
        mainViewModel.showToolbar.observe(this, Observer { showToolbar(it) })
    }

    private fun clearPrefs() {
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }

    private fun openStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        showProgress(false)
        finish()
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_root, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun closeFragment() {
        onBackPressed()
    }

    private fun showDialogFragment(dialog: DialogFragment) {
        dialog.show(supportFragmentManager, null)
    }

    private fun setAppbarTitle(title: String) {
        appbarTitle.text = title
    }

    private fun makeParticipantsToolbar() {
        setAppbarTitle(getString(R.string.who_will_go_with_you))
        appbarTitle.visibility = View.VISIBLE
        appbarSubtitle.visibility = View.VISIBLE
        logoutButton.visibility = View.INVISIBLE
        backButton.visibility = View.VISIBLE
    }

    private fun makeForYouEventsToolbar() {
        showToolbar(true)
        setAppbarTitle(getString(R.string.special_for_you))
        appbarTitle.visibility = View.VISIBLE
        appbarSubtitle.visibility = View.GONE
        logoutButton.visibility = View.VISIBLE
        backButton.visibility = View.GONE
    }

    private fun showToolbar(show: Boolean) {
        toolbar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}