package com.grevi.masakapa.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.DialogBottomSheetBinding
import com.grevi.masakapa.util.Constant.EMPTY_STRING

class DialogBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetBinding
    internal var onButtonClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        observeView()
    }

    private fun observeView() = with(binding) {
        tvTitle.text = arguments?.getString(EXTRA_TITLE) ?: EMPTY_STRING
        tvDescription.text = arguments?.getString(EXTRA_DESCRIPTION) ?: EMPTY_STRING
        btnDone.text = arguments?.getString(EXTRA_BUTTON_TITLE) ?: getString(R.string.ok_text)
    }

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_BUTTON_TITLE = "extra_button_title"

        @JvmStatic
        fun newInstance(
            title: String,
            description: String,
            btnTitle: String? = null
        ) = DialogBottomSheet().apply {
            arguments = bundleOf(
                EXTRA_TITLE to title,
                EXTRA_DESCRIPTION to description,
                EXTRA_BUTTON_TITLE to btnTitle,
            )
        }
    }

}