package com.tinytiger.titi.adapter.gametools.info

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.widget.soulplanet.adapter.PlanetAdapter
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import java.io.UnsupportedEncodingException
import java.util.*

/**
 * @author lmq001
 * @date 2020/05/22 14:53
 * @copyright 小虎互联科技
 * @doc 星球云适配器
 */
class SoulPlanetAdapter : PlanetAdapter() {

    var data: ArrayList<String>? = null
    override fun getCount(): Int {
        return data!!.size
    }

    override fun getView(context: Context?, position: Int, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.game_wiki_item_tag, parent, false)
        val ivLogo = view.findViewById<ImageView>(R.id.ivLogo)
        GlideUtil.loadImg(ivLogo, data!![position])
        return view
    }

    override fun getItem(position: Int): Any? {
        return data!![position]
    }

    override fun getPopularity(position: Int): Int {
        return position % 10
    }

    override fun onThemeColorChanged(view: View?, themeColor: Int) {}
}
