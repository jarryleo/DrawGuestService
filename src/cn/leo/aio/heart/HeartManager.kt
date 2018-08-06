package cn.leo.aio.heart

import cn.leo.aio.service.Channel
import cn.leo.aio.service.ChannelManager
import cn.leo.aio.utils.Constant
import cn.leo.aio.utils.Logger
import java.util.*

object HeartManager : TimerTask() {
    private val lru = LinkedHashMap<Channel, Long>(Constant.maxClient, 0.75f, true)

    override fun run() {
        trim()
    }

    @Synchronized
    fun reflow(channel: Channel) {
        lru[channel] = channel.heartStamp
    }

    //心跳超时检测
    private fun trim() {
        try {
            val iterator = lru.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                val channel = next.key
                val contains = ChannelManager.contains(channel)
                if (!contains) {
                    iterator.remove()
                    continue
                }
                val timestamp = next.value
                if (System.currentTimeMillis() - timestamp > Constant.heartTimeOut) {
                    iterator.remove()
                    channel.close()
                } else {
                    break
                }
            }
        } catch (e: Exception) {
            Logger.e("心跳错误[${lru.size}]:")
            e.printStackTrace()
        }

    }
}