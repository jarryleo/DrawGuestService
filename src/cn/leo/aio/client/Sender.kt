package cn.leo.aio.client

import cn.leo.aio.utils.Logger
import java.nio.channels.CompletionHandler


class Sender : CompletionHandler<Int, AioClient> {
    override fun completed(result: Int?, client: AioClient?) {
        //Logger.d("发送成功")
    }

    override fun failed(exc: Throwable?, client: AioClient?) {
        client?.close()
        Logger.e("发送失败：" + exc.toString())
    }
}