package cn.leo.aio.service

import cn.leo.aio.header.PacketFactory
import cn.leo.aio.heart.HeartManager
import cn.leo.aio.utils.Constant
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel

class Channel(var channel: AsynchronousSocketChannel, serviceListener: ServiceListener) {
    //客户端地址
    val host = (channel.remoteAddress as InetSocketAddress).hostString!!
    val buffer = ByteBuffer.allocate(Constant.packetSize)!!
    //连接接入时间
    val acceptTime: Long = System.currentTimeMillis()
    //上次心跳时间
    var heartStamp: Long = 0
    //通信回调
    private val reader = Reader(serviceListener)
    private val writer = Writer()

    init {
        refreshHeart()
        read()
    }

    fun refreshHeart() {
        heartStamp = System.currentTimeMillis()
        HeartManager.reflow(this)
    }

    fun read() {
        if (!channel.isOpen) return
        buffer.clear()
        channel.read(buffer, this, reader)
    }


    fun send(msg: String, cmd: Short = 0) {
        send(msg.toByteArray(), cmd)
    }

    fun send(data: ByteArray, cmd: Short = 0) {
        if (!channel.isOpen) return
        val bufList = PacketFactory.encodePacketBuffer(data, cmd)
        try {
            var len = 0
            bufList.forEach { len += channel.write(it)!!.get() }
            writer.completed(len, this)
        } catch (e: Exception) {
            writer.failed(e, this)
        }
    }

    //断开连接
    fun close() {
        try {
            ChannelManager.remove(this)
            channel.close()
        } catch (e: Exception) {
        }
    }
}