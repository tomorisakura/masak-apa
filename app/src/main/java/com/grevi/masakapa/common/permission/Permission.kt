package com.grevi.masakapa.common.permission

import android.Manifest
import android.content.Context
import com.grevi.masakapa.R
import com.grevi.masakapa.common.base.BaseActivity
import com.grevi.masakapa.util.Constant
import com.permissionx.guolindev.PermissionX


fun BaseActivity<*>.storagePermission() {
    PermissionX.init(this)
        .permissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .onExplainRequestReason { scope, deniedList, _ ->
            scope.showRequestReasonDialog(deniedList,
                getString(R.string.bucket_permission_reason),
                this.getString(R.string.ok_text),
                this.getString(R.string.cancel_text))
        }
        .request { allGranted, _, _ ->
            if (allGranted) {
                this.getSharedPreferences(Constant.PERMISSIONS_STORAGE, Context.MODE_PRIVATE)
                    .apply {
                        edit().let {
                            it.putBoolean(Constant.PERMISSIONS_STORAGE, true)
                            it.apply()
                        }
                    }
            }
        }
}