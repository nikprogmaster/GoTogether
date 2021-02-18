package com.kandyba.gotogether.presentation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EVENTS_KEY
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.USER_ID
import com.kandyba.gotogether.models.presentation.AuthResponse
import com.kandyba.gotogether.models.presentation.Events
import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.fragment.StartFragment
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel

class StartActivity : AppCompatActivity(), FragmentManager {

    private lateinit var progress: LinearLayout
    private lateinit var animatedLogo: ImageView
    private lateinit var animationLayout: LinearLayout

    private lateinit var prefs: SharedPreferences
    private lateinit var viewModel: StartViewModel
    private lateinit var animation: StartAppAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        resolveDependencies()
        initObservers()
        viewModel.init(prefs)
    }

    private fun initObservers() {
        viewModel.saveUserInfo.observe(this, Observer { response -> saveUserInfo(response) })
        viewModel.showStartFragment.observe(this, Observer {
            openFragment(StartFragment.newInstance())
        })
        viewModel.showHeadpiece.observe(this, Observer { show -> showHeadPiece(show) })
        viewModel.showMainActivity.observe(this, Observer { eventsMap ->
            openMainActivity(Events(eventsMap.values.toList()))
        })
        viewModel.showProgress.observe(this, Observer { show -> showProgress(show) })
    }

    private fun resolveDependencies() {
        animationLayout = findViewById(R.id.animation_layout)
        animatedLogo = findViewById(R.id.animated_logo)
        progress = findViewById(R.id.progress)

        val component = (application as App).appComponent
        prefs = component.getSharedPreferences()
        animation = component.getStartAnimation()

        viewModel = ViewModelProvider(
            this,
            component.getStartViewModelFactory()
        )[StartViewModel::class.java]
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
            .replace(R.id.root, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun closeFragment() {
        onBackPressed()
    }

    override fun openMainActivity(events: Events) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EVENTS_KEY, events)
        startActivity(intent)
        showProgress(false)
        finish()
    }

    override fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun saveUserInfo(authResponse: AuthResponse) {
        val editor = prefs.edit()
        editor.putString(USER_ID, authResponse.user)
        editor.putString(TOKEN, authResponse.token)
        editor.apply()
    }

    private fun showHeadPiece(show: Boolean) {
        if (show) {
            animationLayout.visibility = View.VISIBLE
            animation.setAndStartAnimation(animatedLogo)
        } else {
            animationLayout.visibility = View.GONE
            animation.finishAnimation()
        }
    }

}