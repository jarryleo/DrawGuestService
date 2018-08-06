package cn.leo.aio.service

import cn.leo.aio.utils.Logger
import java.nio.channels.CompletionHandler


class Writer : CompletionHandler<Int, Channel> {
    override fun completed(result: Int?, channel: Channel?) {
        //Logger.d("发送成功")
    }

    override fun failed(exc: Throwable?, channel: Channel?) {
        Logger.e("${channel?.host}写入错误：${exc.toString()}")
        channel?.close()
    }
}