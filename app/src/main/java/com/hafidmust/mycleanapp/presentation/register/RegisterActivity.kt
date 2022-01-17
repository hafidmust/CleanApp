package com.hafidmust.mycleanapp.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hafidmust.mycleanapp.R
import com.hafidmust.mycleanapp.data.common.utils.WrappedResponse
import com.hafidmust.mycleanapp.data.register.remote.dto.RegisterRequest
import com.hafidmust.mycleanapp.data.register.remote.dto.RegisterResponse
import com.hafidmust.mycleanapp.databinding.ActivityRegisterBinding
import com.hafidmust.mycleanapp.domain.register.entity.RegisterEntity
import com.hafidmust.mycleanapp.infrastructure.utils.SharedPrefs
import com.hafidmust.mycleanapp.presentation.common.extension.isEmail
import com.hafidmust.mycleanapp.presentation.common.extension.showGenericAlertDialog
import com.hafidmust.mycleanapp.presentation.common.extension.showToast
import com.hafidmust.mycleanapp.presentation.login.LoginActivityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    @Inject
    lateinit var prefs: SharedPrefs



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUp()
        observe()
        goBack()
    }
    private fun goBack(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    private fun observe(){
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }
    private fun handleState(state: RegisterActivityState){
        when(state){
            is RegisterActivityState.showToast -> showToast(state.message)
            is RegisterActivityState.isLoading -> handleLoading(state.isLoading)
            is RegisterActivityState.init -> Unit
            is RegisterActivityState.errorRegister -> handleErrorRegister(state.rawResponse)
            is RegisterActivityState.successRegister -> handleSuccessRegister(state.registerEntity)
        }
    }

    private fun handleErrorRegister(rawResponse: WrappedResponse<RegisterResponse>) {
        showGenericAlertDialog(rawResponse.message)
    }

    private fun handleSuccessRegister(registerEntity: RegisterEntity) {
        prefs.saveToken(registerEntity.token)
        setResult(RESULT_OK)
        finish()
    }

    private fun handleLoading(loading: Boolean) {
        binding.registerButton.isEnabled = !loading
        binding.loadingProgressBar.isIndeterminate = loading
        binding.backButton.isEnabled = !loading
        if (!loading){
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun signUp(){
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validate(name, email, password)){
                viewModel.register(RegisterRequest(name, email, password))
            }
        }
    }
    private fun validate(name : String, email : String, password : String): Boolean{
        resetAllError()
        if (name.isEmpty()){
            setNameError(getString(R.string.error_name_not_valid))
            return false
        }
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
    private fun setNameError(e : String?){
        binding.nameInput.error = e
    }
    private fun setEmailError(e : String?){
        binding.emailInput.error = e
    }
    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }
    private fun resetAllError(){
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
    }

}