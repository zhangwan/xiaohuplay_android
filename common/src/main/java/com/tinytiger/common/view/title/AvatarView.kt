package com.tinytiger.common.view.title

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.tinytiger.common.R
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.image.GlideUtil
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2020/5/11 0011 下午 3:25
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 头像
 */
class AvatarView : FrameLayout {
    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.view_avatar, this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_avatar, this)
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.AvatarView, 0, 0)
        try { //中间文本内容

            val size = ta.getDimensionPixelSize(R.styleable.AvatarView_TalentSize, 60) / 3
            val ivTalent = findViewById<View>(R.id.ivTalent)
            val lp = ivTalent!!.layoutParams
            lp.width = size
            lp.height = size
            ivTalent.layoutParams = lp
        } finally {
            ta.recycle()
        }
    }


    fun setAvatar(avatar: String?): AvatarView {
        if (avatar != null && avatar.isNotEmpty()) {
            GlideUtil.loadImg(findViewById(R.id.ivIcon), avatar,R.mipmap.icon_default_avatar)
        }
        findViewById<View>(R.id.ivTalent).visibility = View.GONE
        return this
    }

    fun setAvatar(avatar: Int): AvatarView {
        Glide.with(context)
            .load(avatar)
            .into(findViewById(R.id.ivIcon))
        findViewById<View>(R.id.ivTalent).visibility = View.GONE
        return this
    }


    fun setAvatar(avatar: String?, talent: Int): AvatarView {
        if (avatar != null && avatar.isNotEmpty()) {
            GlideUtil.loadLogo(findViewById(R.id.ivIcon), avatar)
        }
        val ivTalent=findViewById<ImageView>(R.id.ivTalent)
        when(talent){
            1->{
                ivTalent.visibility = View.VISIBLE
                ivTalent.setImageResource(R.mipmap.talent_icon_logo_on)
            }
            2->{
                ivTalent.visibility = View.VISIBLE
                ivTalent.setImageResource(R.mipmap.talent_icon_logo_in)
            }
            else->{
                ivTalent.visibility = View.GONE
            }
        }
        return this
    }


    fun setTalent(talent: Int): AvatarView {
        val ivTalent=findViewById<ImageView>(R.id.ivTalent)
        when(talent){
            1->{
                ivTalent.visibility = View.VISIBLE
                ivTalent.setImageResource(R.mipmap.talent_icon_logo_on)
            }
            2->{
                ivTalent.visibility = View.VISIBLE
                ivTalent.setImageResource(R.mipmap.talent_icon_logo_in)
            }
            else->{
                ivTalent.visibility = View.GONE
            }
        }
        return this
    }

    fun setUserId(user: String) {
        findViewById<View>(R.id.ivIcon).setOnClickListener {
           EventBus.getDefault().post(ClassEvent("HomepageActivity", user))
        }
    }
}