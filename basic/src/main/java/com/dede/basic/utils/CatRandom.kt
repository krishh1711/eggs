package com.dede.basic.utils

/**
 * Cat Eggs Random impl
 *
 * @author shhu
 * @since 2022/8/23
 */
object CatRandom {

    @JvmStatic
    fun get(): java.util.Random {
        //return ThreadLocalRandom.current()// default
        return java.util.Random()
    }

}