package cn.leo.aio.service

class Client(private val channel: Channel) {
    val ip = channel.host

    val acceptTime = channel.acceptTime

    fun send(msg: String, cmd: Short = 0) {
        channel.send(msg.toByteArray(), cmd)
    }

    fun send(data: ByteArray, cmd: Short = 0) {
        channel.send(data, cmd)
    }

    fun close() {
        channel.close()
    }

    override fun equals(other: Any?): Boolean {
        val client = other as? Client
        return channel === client?.channel
    }

    override fun hashCode(): Int {
        return channel.hashCode()
    }
}