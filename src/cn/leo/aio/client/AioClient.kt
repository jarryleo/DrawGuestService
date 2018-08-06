package cn.leo.aio.client

import cn.leo.aio.header.PacketFactory
import cn.leo.aio.utils.Constant
import cn.leo.aio.utils.Logger
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

/**
 * create by : Jarry Leo
 * date : 2018/7/31 16:33
 */
class AioClient {
    val buffer = ByteBuffer.allocate(Constant.packetSize)!!
    private var lastHeartStamp: Long = 0
    private var client: AsynchronousSocketChannel? = null
    private var serverAddress: InetSocketAddress? = null
    private lateinit var receiver: Receiver
    private val sender = Sender()
    private var mClientListener: ClientListener? = null

    fun connect(host: String, port: Int, clientListener: ClientListener) {
        mClientListener = clientListener
        receiver = Receiver(clientListener)
        serverAddress = InetSocketAddress(host, port)
        connect()
    }

    private fun connect() {
        client = AsynchronousSocketChannel.open()
        client?.connect(serverAddress, buffer, handler)
    }

    //连接服务器结果回调
    private val handler =
            object : CompletionHandler<Void, ByteBuffer> {
                override fun completed(p0: Void?, p1: ByteBuffer?) {
                    receive()//开始接收数据
                    Heart(this@AioClient)//开启心跳
                    mClientListener?.onConnectSuccess()
                    Logger.d("连接服务器成功")
                }

                override fun failed(p0: Throwable?, p1: ByteBuffer?) {
                    Logger.e("连接失败！" + p0.toString())
                    mClientListener?.onConnectFailed()
                    reconnect()
                }
            }

    //关闭连接
    fun close() {
        try {
            client?.close()
        } catch (e: Exception) {
            mClientListener?.onConnectInterrupt()
        } finally {
            reconnect()
        }
    }

    //重连
    fun reconnect() {
        try {
            Thread.sleep(5000)
        } finally {
            connect()//重连，这里可以设置重连间隔递增和超时操作
        }
    }

    //发送文本，默认UTF-8
    fun send(msg: String, cmd: Short = 0) {
        send(msg.toByteArray(), cmd)
    }

    //发送字节数组
    fun send(data: ByteArray, cmd: Short = 0) {
        if (!client?.isOpen!!) return
        val bufList = PacketFactory.encodePacketBuffer(data, cmd)
        try {
            var len = 0
            bufList.forEach { len += client?.write(it)!!.get() }
            sender.completed(len, this)
            lastHeartStamp = System.currentTimeMillis()
        } catch (e: Exception) {
            sender.failed(e, this)
        }
    }

    //接收数据
    fun receive() {
        if (!client?.isOpen!!) return
        buffer.clear()
        client?.read(buffer, this, receiver)
    }

    //心跳
    fun heart() {
        if (System.currentTimeMillis() - lastHeartStamp > Constant.heartTimeOut / 2) {
            send("${System.currentTimeMillis()}", Constant.heartCmd)
        }
    }
}