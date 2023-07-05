package com.example.stateflowsample.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.stateflowsample.databinding.ActDetailBinding
import com.example.stateflowsample.launchWhenStartedRepeat
import kotlinx.coroutines.flow.collectLatest

class DetailAct : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var binding: ActDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retryBtn.setOnClickListener {
            viewModel.load()
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        launchWhenStartedRepeat {
            viewModel.state.collectLatest { state ->
                binding.toolbar.title = state.title
                binding.failureGroup.isVisible = state.failureMessage.isNotEmpty()
                binding.failureMessageTv.text = state.failureMessage
                binding.progressBar.isVisible = state.loading
                binding.descriptionTv.text = state.description
            }
        }
    }
}