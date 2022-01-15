package com.hafidmust.mycleanapp.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hafidmust.mycleanapp.R
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginRequest
import com.hafidmust.mycleanapp.data.login.remote.dto.LoginResponse
import com.hafidmust.mycleanapp.databinding.ActivityLoginBinding
import com.hafidmust.mycleanapp.domain.login.entity.LoginEntity
import com.hafidmust.mycleanapp.infrastructure.utils.SharedPrefs
import com.hafidmust.mycleanapp.presentation.common.extension.isEmail
import com.hafidmust.mycleanapp.presentation.common.extension.showGenericAlertDialog
import com.hafidmust.mycleanapp.presentation.common.extension.showToast
import com.hafidmust.mycleanapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var pref: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login()
        observe()
    }

    private fun login(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validate(email, password)){
                viewModel.login(LoginRequest(email = email,password = password))
            }
        }
    }

    private fun observe(){
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: LoginActivityState){
        when(state){
            is LoginActivityState.showToast -> showToast(state.message)
            is LoginActivityState.isLoading -> handleLoading(state.isLoading)
            is LoginActivityState.Init -> Unit
            is LoginActivityState.errorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.successLogin -> handleSuccessLogin(state.loginEntity)
        }
    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<LoginResponse>) {
        showGenericAlertDialog(rawResponse.message)
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity) {
        pref.saveToken(loginEntity.token)
        showToast("Welcome ${loginEntity.name}")
        goToMainActivity()
    }

    private fun goToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleLoading(loading: Boolean) {
        binding.loginButton.isEnabled = !loading
        binding.loadingProgressBar.isIndeterminate = loading
        if (!loading){
            binding.loadingProgressBar.progress = 0
        }
    }


    private fun setEmailError(e : String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e : String?){
        binding.passwordInput.error = e
    }

    private fun resetAllError(){
        setEmailError(null)
        setPasswordError(null)
    }

    private fun validate(email : String, password : String) : Boolean{
        resetAllError()
        if (!email.isEmail()){
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }
        if (password.length < 8){
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }
        return true
    }
}