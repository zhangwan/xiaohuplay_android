package com.tinytiger.titi.ui.test.vp

import android.graphics.Bitmap
import com.tinytiger.common.basis.BasisView
import com.yanzhenjie.album.mvp.BaseView


interface LoginView : BasisView {
    fun setVerificationCode(bitmap: Bitmap)

    fun startCountDown()

    fun finishActivity()

    fun startChange()

}