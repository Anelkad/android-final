package com.example.finalproject.fragments

import android.app.Dialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.google.android.material.snackbar.Snackbar

open class BaseFragment: Fragment() {
    private lateinit var waitDialog: Dialog

    fun showSnackBar(message: String, isError: Boolean) {
        val snackBar =
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

        if (isError) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(requireActivity(), R.color.colorSnackBarError)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(requireActivity(), R.color.colorSnackBarSuccess)
            )
        }
        snackBar.show()
    }

    fun showWaitDialog(){
        waitDialog = Dialog(requireActivity())
        waitDialog.setContentView(R.layout.wait_dialog)

        waitDialog.setCancelable(false)
        waitDialog.setCanceledOnTouchOutside(false)

        waitDialog.show()
    }

    fun hideWaitDialog(){
        if (this::waitDialog.isInitialized) waitDialog.dismiss()
    }
}