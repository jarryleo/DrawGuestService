package cn.leo.aio.service

import java.util.concurrent.atomic.AtomicInteger

object Service {
    private val mCount = AtomicInteger(1)
    fun start(port: Int, serviceListener: ServiceListener) {
        object : Thread("Service #${mCount.getAndIncrement()}") {
            override fun run() {
                AioServiceCore().start(port, serviceListener)
            }
        }.start()
    }
}