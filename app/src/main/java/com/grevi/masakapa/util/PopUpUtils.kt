package com.grevi.masakapa.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun toast(context: Context, msg : String) : Toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
fun snackBar(view: View, msg: String) : Snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)