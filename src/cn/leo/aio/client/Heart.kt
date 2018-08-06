package cn.leo.aio.client

import cn.leo.aio.utils.Constant
import java.util.*

class Heart(var client: AioClient) : TimerTask() {
    companion object {
        var timer: Timer? = null
    }

    init {
        if (timer != null) {
            timer!!.cancel()
        }
        timer = Timer()
        timer!!.schedule(this, Constant.heartTimeOut / 2 - 1, Constant.heartTimeOut / 2 - 1)
    }

    override fun run() {
        client.heart()
    }
}