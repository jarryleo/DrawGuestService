package cn.leo.aio.service

import cn.leo.aio.header.Packet
import cn.leo.aio.header.PacketFactory
import cn.leo.aio.utils.Constant
import cn.leo.aio.utils.Logger
import java.nio.channels.CompletionHandler


class Reader(private val serviceListener: ServiceListener) : CompletionHandler<Int, Channel> {
    private var cache: Packet? = null
    override fun completed(result: Int?, channel: Channel?) {
        if (result!! >= 0) {
            val buffer = channel?.buffer
            if (cache == null) {
                cache = PacketFactory.decodePacketBuffer(buffer!!)
            } else {
                val packet = cache!!.addData(buffer!!)
                if (packet != cache) {
                    notifyData(channel, cache!!.data, cache!!.cmd)
                    cache = packet
                }
            }
            //校验数据包版本，不符合断开连接
            if (Constant.checkVersion && cache!!.ver != Constant.version) {
                channel.close()
                return
            }
            //数据包完整后
            if (cache!!.isFull()) {
                notifyData(channel, cache!!.data, cache!!.cmd)
                cache = null
            }
            channel.read() //继续接收下一波数据
        } else {
            channel?.close()
        }
    }

    private fun notifyData(channel: Channel?, data: ByteArray?, cmd: Short) {
        //是心跳数据包过滤数据，刷新链接心跳时间
        channel!!.refreshHeart()
        if (cmd == Constant.heartCmd) {
            channel.send("${System.currentTimeMillis()}", Constant.heartCmd) //回复客户端心跳
            return
        }
        val client = ChannelManager.getClient(channel)
        serviceListener.onDataArrived(client!!, data!!, cmd)
    }

    override fun failed(exc: Throwable?, channel: Channel?) {
        //Logger.e("${channel?.host}读取错误：${exc.toString()}")
        channel?.close()
    }
}