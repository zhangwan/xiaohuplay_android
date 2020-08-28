package com.tinytiger.common.event

import com.tinytiger.common.R
import com.tinytiger.common.utils.preference.SpUtils

/**
 * clas
 */
class TestEvent(
    mclass: String,
    map: MutableMap<String, Any>
) {
    var title: String

    init {
        val addr = StringBuilder()
        addr.append("事件:$mclass")
        addr.append("\n渠道:${SpUtils.getString(R.string.channel, "titi")}")
        for (key in map.keys) {
            addr.append("\n" + key + " : " + map[key])
        }

        title = addr.toString()
    }
}