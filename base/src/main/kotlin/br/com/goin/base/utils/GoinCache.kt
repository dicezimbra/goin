package br.com.goin.base.utils

import android.app.ActivityManager
import android.content.Context
import android.util.LruCache
import br.com.goin.base.BaseApplication

class GoinCache(context: Context){

    companion object {
        val instance by lazy { GoinCache(BaseApplication.instance.context!!) }
    }

    private var cache: LruCache<String, Any>

    init{
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val memoriaDisponivel = am!!.memoryClass * 1024 * 1024
        cache = LruCache(memoriaDisponivel / 8)
    }

    fun remove(key: String) {
        cache.remove(key)
    }

    fun <T> read(key: String): T? {
      return cache.get(key) as T
    }

    fun <T> write(key: String, any: T) {
        cache.put(key, any)
    }
}