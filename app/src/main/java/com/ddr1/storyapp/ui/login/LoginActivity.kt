package com.ddr1.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.ddr1.storyapp.R
import com.ddr1.storyapp.databinding.ActivityLoginBinding
import com.ddr1.storyapp.ui.main.MainActivity
import com.ddr1.storyapp.ui.register.RegisterActivity
import com.ddr1.storyapp.view.showGenericAlertDialog
import com.ddr1.storyapp.view.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActions()
        observe()
    }

    private fun setActions() {
        binding.apply {
            tvRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).also { intent ->
                    startActivity(intent)
                }
            }
            btnLogin.setOnClickListener {
                userLogin()
            }
        }
    }

    private fun userLogin() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            lifecycleScope.launchWhenResumed {
                viewModel.login(email, password)
                    .onStart {
                        handleLoading(true)
                    }
                    .collect { result ->
                        handleLoading(false)
                        result.onSuccess {
                            viewModel.saveAuthToken(it.loginResult.token)
                            handleSuccessLogin(getString(R.string.login_success))
                        }
                        result.onFailure {
                            handleErrorLogin(getString(R.string.login_failed))
                        }
                    }
            }
        } else {
            showToast(getString(R.string.fill_in_all_fields))
        }
    }

    private fun observe() {
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: LoginActivityState) {
        when (state) {
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.Init -> Unit
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private suspend fun handleSuccessLogin(message: String) {
        val token = viewModel.getUserToken().first()
        showToast(message)
        setResult(RESULT_OK)
        if (token != null) {
            Intent(this@LoginActivity, MainActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }
    }

    private fun handleErrorLogin(message: String) {
        showGenericAlertDialog(message)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.apply {
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading
            loadingProgressBar.isIndeterminate = isLoading
            if (!isLoading) {
                loadingProgressBar.progress = 0
            }
        }
    }
}