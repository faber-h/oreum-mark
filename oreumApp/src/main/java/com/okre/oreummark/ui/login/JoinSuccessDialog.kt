package com.okre.oreummark.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.okre.oreummark.common.THROTTLE_DURATION
import com.okre.oreummark.databinding.DialogJoinSuccessBinding
import com.okre.oreummark.ui.main.MainActivity
import java.util.concurrent.TimeUnit

class JoinSuccessDialog : DialogFragment() {
    companion object {
        fun newInstance() = JoinSuccessDialog()
    }

    private lateinit var binding: DialogJoinSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogJoinSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.joinSuccessBtn.clicks()
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                dismiss()
                startActivity(Intent(context, MainActivity::class.java))
                requireActivity().finish()
            }
    }
}