package com.example.finalproject.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalproject.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var waitDialog: Dialog

    fun showSnackBar(message: String, isError: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBar.setTextColor(getColor(R.color.white))

        if (isError) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorSnackBarError)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorSnackBarSuccess)
            )
        }
        snackBar.show()
    }

    fun showWaitDialog(){
        waitDialog = Dialog(this)
        waitDialog.setContentView(R.layout.wait_dialog)

        waitDialog.setCancelable(false)
        waitDialog.setCanceledOnTouchOutside(false)

        waitDialog.show()
    }

    fun hideWaitDialog(){
        waitDialog.dismiss()
    }
}