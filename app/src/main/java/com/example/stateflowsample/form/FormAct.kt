package com.example.stateflowsample.form

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.stateflowsample.databinding.ActFormBinding
import com.example.stateflowsample.launchWhenStartedRepeat
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "FormAct"

class FormAct : AppCompatActivity() {
    private val viewModel: FormViewModel by viewModels()

    private lateinit var binding: ActFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            viewModel.submit()
        }
        binding.phoneEt.onTextChange(viewModel::onPhoneChange)

        binding.nameEt.onTextChange(viewModel::onNameChange)

        binding.otpEt.onTextChange(viewModel::onOtpChange)

        binding.resendBtn.setOnClickListener {
            viewModel.sendOtp()
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        launchWhenStartedRepeat {
            viewModel.state.collectLatest { state ->
                binding.apply {
                    nameTil.error = state.nameError
                    phoneTil.error = state.phoneError
                    otpTil.error = state.otpError
                    progressBar.isVisible = state.loading
                    submitBtn.isVisible = !state.loading
                    resendBtn.isVisible = state.otpFieldVisible && state.otpTimerValueInSec == 0
                    otpTil.isVisible = state.otpFieldVisible
                    resendOtpTimer.isVisible =
                        state.otpFieldVisible && state.otpTimerValueInSec != 0
                    resendOtpTimer.text = "Resend OTP in ${state.otpTimerValueInSec}s"
                }
            }
        }

        launchWhenStartedRepeat {
            viewModel.actionResultSharedFlow.collectLatest { action ->
                when (action) {
                    FormActionResult.Success -> onBackPressedDispatcher.onBackPressed()
                    is FormActionResult.Failure -> Toast.makeText(
                        this@FormAct,
                        action.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {}
                }
            }
        }
    }
}

inline fun EditText.onTextChange(crossinline action: (text: String) -> Unit) {
    doOnTextChanged { text, _, _, _ ->
        action(text?.toString().orEmpty())
    }
}