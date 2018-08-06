package cn.leo.aio.service

import cn.leo.aio.utils.Logger
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object ChannelManager {
    private var channelList = Collections.synchronizedList(ArrayList<Channel>())
    private var clientList = ConcurrentHashMap<Channel, Client>()

    fun add(channel: Channel) {
        channelList.add(channel)
        clientList[channel] = Client(channel)
        Logger.d("有客户端接入:${channel.host}[总:${channelList.size}]")
    }

    fun remove(channel: Channel) {
        val remove = channelList.remove(channel)
        clientList.remove(channel)
        if (remove) {
            Logger.d("有客户端断开:${channel.host}[总:${channelList.size}]")
        }
    }

    //通过频道获取客户端
    fun getClient(channel: Channel) = clientList[channel]

    fun sendMsgToAll(data: ByteArray) {
        channelList.forEach { it.send(data) }
    }

    fun contains(channel: Channel) = channelList.contains(channel)
}