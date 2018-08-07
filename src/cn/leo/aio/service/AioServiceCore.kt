package cn.leo.aio.service

import cn.leo.aio.heart.HeartManager
import cn.leo.aio.utils.Constant
import cn.leo.aio.utils.Logger
import cn.leo.aio.utils.ThreadPool
import java.net.InetSocketAddress
import java.net.StandardSocketOptions
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.*


internal class AioServiceCore {
    //并发线程池，根据业务自定义
    private val executorService = ThreadPool.getThreadPool()
    private val channelGroup = AsynchronousChannelGroup.withThreadPool(executorService)
    private val service = AsynchronousServerSocketChannel.open(channelGroup)
    private var mServiceListener: ServiceListener? = null

    fun start(port: Int, serviceListener: ServiceListener) {
        val serverAddress = InetSocketAddress(port)
        mServiceListener = serviceListener
        try {
            //通过setOption配置Socket
            service.setOption(StandardSocketOptions.SO_REUSEADDR, true)//重用地址
            service.setOption(StandardSocketOptions.SO_RCVBUF, Constant.packetSize)
            service.bind(serverAddress)
            asyncAccept()
            checkHeart()
            Logger.i("服务器开启成功(端口号:$port)")
        } catch (e: Exception) {
            Logger.e("服务器开启错误：" + e.toString())
        }
    }

    //异步接入方法
    private fun asyncAccept() {
        val buffer = ByteBuffer.allocate(Constant.packetSize)
        service.accept(buffer, handler)
    }

    //接入回调
    private val handler =
            object : CompletionHandler<AsynchronousSocketChannel, ByteBuffer> {
                override fun completed(clientChannel: AsynchronousSocketChannel?, p1: ByteBuffer?) {
                    asyncAccept()
                    val channel = Channel(clientChannel!!, mServiceListener!!)
                    val client = ChannelManager.add(channel)
                    mServiceListener!!.onNewConnectComing(client)
                }

                override fun failed(p0: Throwable?, p1: ByteBuffer?) {
                    Logger.e("接入错误：" + p0.toString())
                }
            }

    //开启心跳检测
    private fun checkHeart() {
        Timer().schedule(HeartManager, Constant.heartTimeOut, Constant.heartTimeOut)
    }

}