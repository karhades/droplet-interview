package com.karipidis.droplet.presentation.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.karipidis.droplet.R
import com.karipidis.droplet.presentation.details.DetailsActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : AppCompatActivity() {

    private val viewModel: WelcomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        continue_button.setOnClickListener { viewModel.handleEntrance() }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.run {
            val owner = this@WelcomeActivity
            viewModel.startLogin.observe(owner, Observer { startLogin() })
            viewModel.startDetails.observe(owner, Observer {
                startDetails(it)
                finish()
            })
            viewModel.message.observe(owner, Observer { showMessage(it) })
        }
    }

    private fun startLogin() {
        val intent = getLoginIntent()
        startActivityForResult(intent, REQUEST_LOGIN)
    }

    private fun getLoginIntent(): Intent {
        return AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.PhoneBuilder().build()))
            .setIsSmartLockEnabled(false)
            .build()
    }

    private fun startDetails(userId: String) {
        val intent = DetailsActivity.newIntent(this, userId)
        startActivity(intent)
    }

    private fun showMessage(@StringRes stringRes: Int) {
        Snackbar.make(root_layout, stringRes, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            val idpResponse = IdpResponse.fromResultIntent(data)
            viewModel.handleLoginResponse(resultCode, idpResponse)
        }
    }

    companion object {

        private const val REQUEST_LOGIN = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, WelcomeActivity::class.java)
        }
    }
}
