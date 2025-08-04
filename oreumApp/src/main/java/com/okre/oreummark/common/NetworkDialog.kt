package com.okre.oreummark.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.okre.oreummark.databinding.DialogNetworkBinding

/**
 * Network 비연결 시 다시 시도를 요청하는 대화상자
 */
class NetworkDialog : DialogFragment() {
    interface DialogListener {
        fun onDialogPositiveClick()
    }

    // 대화상자를 띄운 화면에 콜백을 요청
    private lateinit var binding: DialogNetworkBinding
    var myDialogInterface: DialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {
            myDialogInterface!!.onDialogPositiveClick()
        }
    }
}