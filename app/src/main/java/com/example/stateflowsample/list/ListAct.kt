package com.example.stateflowsample.list

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.stateflowsample.databinding.ActListBinding
import com.example.stateflowsample.detail.DetailAct
import com.example.stateflowsample.form.FormAct
import com.example.stateflowsample.launchWhenStartedRepeat
import kotlinx.coroutines.flow.collectLatest

class ListAct : AppCompatActivity() {
    private val viewModel: ListViewModel by viewModels()

    private lateinit var binding: ActListBinding

    companion object {
        const val LIST_ITEM = "LIST_ITEM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retryBtn.setOnClickListener {
            viewModel.load()
        }

        binding.supportBtn.setOnClickListener {
            startActivity(Intent(this, FormAct::class.java))
        }

        launchWhenStartedRepeat {
            viewModel.state.collectLatest { state ->
                binding.failureGroup.isVisible = state.failureMessage.isNotEmpty()
                binding.failureMessageTv.text = state.failureMessage
                binding.progressBar.isVisible = state.loading

                if (state.data.isNotEmpty()) {
                    binding.linearLayout.isVisible = true
                    binding.linearLayout.removeAllViews()
                    state.data.forEach { row ->
                        val textView = TextView(this@ListAct).apply {
                            text = row.name
                            textSize = 16f
                            setTextColor(
                                ContextCompat.getColor(
                                    this@ListAct,
                                    android.R.color.black
                                )
                            )
                            typeface = Typeface.DEFAULT_BOLD
                            setPadding(16, 16, 16, 16)
                            setOnClickListener {
                                startActivity(
                                    Intent(
                                        this@ListAct,
                                        DetailAct::class.java
                                    ).apply { putExtra(LIST_ITEM, row) })
                            }
                        }
                        binding.linearLayout.addView(textView)
                    }
                }
            }
        }
    }
}