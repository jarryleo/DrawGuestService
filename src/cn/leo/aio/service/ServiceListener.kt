package cn.leo.aio.service

interface ServiceListener {
    /**
     * 有新的连接
     */
    fun onNewConnectComing(client: Client)

    /**
     * 有连接中断
     */
    fun onConnectInterrupt(client: Client)

    /**
     * 有数据抵达
     * @param client 数据来源客户端
     * @param data 二进制数据
     * @param cmd 数据类型，最好是正整数
     */
    fun onDataArrived(client: Client, data: ByteArray, cmd: Short)
}