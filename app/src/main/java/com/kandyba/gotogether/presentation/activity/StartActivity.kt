package com.kandyba.gotogether.presentation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.auth.LoginDomainResponse
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.USER_ID
import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.fragment.prelogin.StartFragment
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel


class StartActivity : AppCompatActivity(), FragmentManager {

    private lateinit var progress: LinearLayout
    private lateinit var animatedLogo: ImageView
    private lateinit var animationLayout: LinearLayout
    private lateinit var root: FrameLayout

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
        viewModel.showStartFragment.observe(this, Observer {
            openFragment(StartFragment.newInstance())
        })
        viewModel.showHeadpiece.observe(this, Observer { show -> showHeadPiece(show) })
        viewModel.showMainActivity.observe(this, Observer {
            openMainActivity()
        })
        viewModel.showProgress.observe(this, Observer { show -> showProgress(show) })
        viewModel.showSnackbar.observe(this, Observer { mes -> showSnackbar(mes.message) })
        viewModel.loginResponse.observe(this, Observer { response -> saveUserInfo(response) })
    }

    private fun resolveDependencies() {
        animationLayout = findViewById(R.id.animation_layout)
        animatedLogo = findViewById(R.id.animated_logo)
        progress = findViewById(R.id.progress)
        root = findViewById(R.id.root)

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

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        showProgress(false)
        finish()
    }

    override fun showDialogFragment(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    private fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun clearPrefs() {
        val editor = prefs.edit()
        editor.clear().apply()
        Cache.instance.clearAllCache()
    }

    private fun saveUserInfo(authResponse: LoginDomainResponse) {
        val editor = prefs.edit()
        editor.putString(USER_ID, authResponse.userId)
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

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(root, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

}