/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package library.jdp.jdplib.listener

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.util.Log

import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import library.jdp.jdplib.R


abstract class MultiplePermissionListener(private val activity: Activity) : MultiplePermissionsListener {

    protected abstract fun denied(permissionName: String)
    protected abstract fun granted(permissionName: String)

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        for (response in report.grantedPermissionResponses) {
            Log.e("permission:", response.permissionName + ": granted")
            granted(response.permissionName)
        }
        for (response in report.deniedPermissionResponses) {
            Log.e("permission:", response.permissionName + ": denied")
            denied(response.permissionName)
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
        showPermissionRationale(token)
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showPermissionRationale(token: PermissionToken) {
        AlertDialog.Builder(activity).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    dialog.dismiss()
                    token.cancelPermissionRequest()
                }
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    dialog.dismiss()
                    token.continuePermissionRequest()
                }
                .setOnDismissListener { token.cancelPermissionRequest() }.show()
    }
}
