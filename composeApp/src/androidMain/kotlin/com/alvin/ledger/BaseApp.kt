package com.alvin.ledger

import android.app.Application

/**
 * 类描述
 *
 * @author 高国峰
 * @date 2023/12/8-11:23
 */
class BaseApp : Application() {

    companion object{
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}