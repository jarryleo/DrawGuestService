package cn.leo.aio.client

interface ClientListener {
    fun onConnectSuccess()
    fun onConnectFailed()
    fun onConnectInterrupt()
    /**
     * 有数据抵达
     * @param data 二进制数据
     * @param cmd 数据类型，最好是正整数
     */
    fun onDataArrived(data: ByteArray, cmd: Short)
}