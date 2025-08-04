package com.okre.oreummark.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.DialogJoinTermContentBinding
import java.util.concurrent.TimeUnit

class JoinTermContent : AppCompatActivity() {

    private lateinit var binding: DialogJoinTermContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogJoinTermContentBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        binding.joinTermContentBack.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        binding.joinTermContentWebView.loadUrl("file:///android_asset/term.html")
    }
}