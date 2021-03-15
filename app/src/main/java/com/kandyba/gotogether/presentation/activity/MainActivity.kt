package com.kandyba.gotogether.presentation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.EVENTS_KEY
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.presentation.Events
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.fragment.main.ForYouFragment
import com.kandyba.gotogether.presentation.viewmodel.ForYouViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.ForYouViewModelFactory


class MainActivity : AppCompatActivity(), FragmentManager {

    private lateinit var toolbar: Toolbar
    private lateinit var logoutButton: ImageView
    private lateinit var cancelAlertButton: Button
    private lateinit var confirmAlertButton: Button
    private lateinit var alertDialog: AlertDialog
    private lateinit var progress: LinearLayout
    private lateinit var mainLayout: ConstraintLayout

    private lateinit var factory: ForYouViewModelFactory
    private lateinit var viewModel: ForYouViewModel
    private lateinit var settings: SharedPreferences

    private lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resolveDependencies()
        initListeners()
        initObservers()
        openFragment(ForYouFragment.newInstance(events))
    }

    private fun resolveDependencies() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        progress = findViewById(R.id.progress)

        //init di
        settings = (application as App).appComponent.getSharedPreferences()
        factory = (application as App).appComponent.getForYouViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[ForYouViewModel::class.java]

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

        //init Events
        events = intent.extras?.getSerializable(EVENTS_KEY) as Events
    }

    private fun initListeners() {
        logoutButton.setOnClickListener {
            cancelAlertButton.setOnClickListener { alertDialog.dismiss() }
            confirmAlertButton.setOnClickListener {
                viewModel.logout(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
            }
            alertDialog.show()
        }
    }

    private fun initObservers() {
        viewModel.showProgress.observe(this, Observer { show -> showProgress(show) })
        viewModel.showSnackbar.observe(this, Observer { mes -> showSnackbar(mes.message) })
        viewModel.logoutCompleted.observe(this, Observer {
            showProgress(true)
            clearPrefs()
            openStartActivity()
        })
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

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_root, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun closeFragment() {
        TODO("Not yet implemented")
    }

    override fun openMainActivity(events: Events) {
        TODO("Not yet implemented")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}