package com.tinytiger.titi.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.post.SendPostActivity

/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:29
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 发帖首页发帖按钮
 */
class CircleSendView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var ivAdd: ImageView? = null
    private var ivPost1: ImageView? = null
    private var ivPost2: ImageView? = null
    private var vView: View? = null
    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_circle_send, this)
        ivAdd = view.findViewById(R.id.ivAdd)
        ivPost1 = view.findViewById(R.id.ivPost1)
        ivPost2 = view.findViewById(R.id.ivPost2)
         vView = view.findViewById<View>(R.id.vView)
        vView?.setOnClickListener {
            setsenoc45()
        }
        ivAdd?.setOnClickListener {
            setsenoc45()
        }


        ivPost1?.setOnClickListener {
            SendPostActivity.actionStart(context)
        }

        ivPost2?.setOnClickListener {
            SendPostActivity.actionStart(context)
        }
    }

    init {
        init(context)
    }


    private var showType=false
    private fun setsenoc45() {
        showType=!showType

        var fron = 0f
        var to = 45f
        if (!showType) {
            fron = 45f
            to = 0f
            vView?.visibility = View.GONE
        } else {
            vView?.visibility = View.VISIBLE
        }

        val anim: Animation =
            RotateAnimation(fron, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.setFillAfter(true)
        anim.setDuration(200)
        anim.setInterpolator(AccelerateInterpolator())

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if (showType) {
                    ivPost1?.visibility = View.VISIBLE
                    ivPost2?.visibility = View.VISIBLE
                } else {
                    ivPost1?.visibility = View.GONE
                    ivPost2?.visibility = View.GONE
                }
            }

            override fun onAnimationStart(p0: Animation?) {

            }

        })
        ivAdd?.startAnimation(anim)
    }

}