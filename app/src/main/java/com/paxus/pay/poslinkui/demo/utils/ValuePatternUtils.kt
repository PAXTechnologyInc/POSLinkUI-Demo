package com.paxus.pay.poslinkui.demo.utils

/**
 * Utils for parse value patten
 */
object ValuePatternUtils {
    fun getMaxLength(lengthRange: String): Int {
        val list = getLengthList(lengthRange)
        return list.lastOrNull()
            ?: throw IllegalArgumentException("Invalid lengthRange (empty): $lengthRange")
    }

    fun getMinLength(lengthRange: String): Int {
        val list = getLengthList(lengthRange)
        return list.firstOrNull()
            ?: throw IllegalArgumentException("Invalid lengthRange (empty): $lengthRange")
    }

    fun getLengthList(lengthRange: String): List<Int?> {
        val range = lengthRange.split("[,]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val lenList: MutableList<Int?> = ArrayList()
        for (len in range) {
            if (len.contains("-")) {
                val bounds = len.split("-".toRegex()).map { it.trim() }.filter { it.isNotEmpty() }
                if (bounds.size < 2) continue
                val start = bounds[0].toInt()
                val end = bounds[1].toInt()
                for (j in start..end) {
                    lenList.add(j)
                }
            } else {
                if (len.isNotEmpty()) lenList.add(len.toInt())
            }
        }
        return lenList.toList()
    }
}
