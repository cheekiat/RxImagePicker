package com.qingmei2.rximagepicker.ui.camera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.BaseSystemPickerView
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import io.reactivex.Observable

class SystemCameraPickerView : BaseSystemPickerView(), ICameraCustomPickerView {

    override fun display(fragmentActivity: FragmentActivity,
                         @IdRes containerViewId: Int,
                         tag: String,
                         configuration: ICustomPickerConfiguration) {
        val fragmentManager = fragmentActivity.getSupportFragmentManager()
        val fragment = fragmentManager.findFragmentByTag(tag) as SystemCameraPickerView
        if (fragment == null) {
            val transaction = fragmentManager.beginTransaction()
            if (containerViewId != 0) {
                transaction.add(containerViewId, this, tag).commit()
            } else {
                transaction.add(this, tag).commit()
            }
        }
    }

    override fun pickImage(): Observable<Result> {
        return uriObserver
    }

    override fun startRequest() {
        if (!checkPermission()) {
            return
        }
        cameraPictureUrl = createImageUri()
        val pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)

        startActivityForResult(pictureChooseIntent, BaseSystemPickerView.CAMERA_REQUEST_CODE)
    }

    override fun getActivityResultUri(data: Intent?): Uri? {
        return cameraPictureUrl
    }

    private fun createImageUri(): Uri? {
        val contentResolver = getActivity()!!.getContentResolver()
        val cv = ContentValues()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        cv.put(MediaStore.Images.Media.TITLE, timeStamp)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    }

    companion object {

        private var cameraPictureUrl: Uri? = null
    }
}
