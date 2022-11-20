package com.ddr1.storyapp.ui.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ddr1.storyapp.R
import com.ddr1.storyapp.databinding.ActivityRegisterBinding
import com.ddr1.storyapp.view.showGenericAlertDialog
import com.ddr1.storyapp.view.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setActions()
        observe()
    }

    private fun setActions() {
        binding.apply {
            btnRegister.setOnClickListener {
                userRegister()
            }

            tvLogin.setOnClickListener {
                finish()
            }
        }
    }

    private fun userRegister() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            lifecycleScope.launchWhenResumed {
                viewModel.register(name, email, password)
                    .onStart {
                        handleLoading(true)
                    }
                    .collect { result ->
                        handleLoading(false)
                        result.onSuccess {
                            handleSuccessRegister(getString(R.string.user_created))
                        }
                        result.onFailure {
                            handleErrorRegister("Failed to register, please try again")
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

    private fun handleStateChange(state: RegisterActivityState) {
        when (state) {
            is RegisterActivityState.ShowToast -> showToast(state.message)
            is RegisterActivityState.Init -> Unit
        }
    }

    private fun handleSuccessRegister(message: String) {
        showToast(message)
        setResult(RESULT_OK)
        finish()
    }

    private fun handleErrorRegister(message: String) {
        showGenericAlertDialog(message)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.apply {
            nameEditText.isEnabled = !isLoading
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading
            loadingProgressBar.isIndeterminate = isLoading
            if (!isLoading) {
                loadingProgressBar.progress = 0
            }
        }
    }
}