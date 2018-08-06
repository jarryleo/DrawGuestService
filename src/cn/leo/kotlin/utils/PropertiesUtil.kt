package cn.leo.kotlin.utils

import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

object PropertiesUtil {
    private lateinit var path: String
    private lateinit var user: String
    private lateinit var psw: String
    private var port: Int = 0

    init {
        val p = Properties()
        try {
            p.load(FileInputStream("db.properties")) // 读取配置文件
            val driver = p.getProperty("driver") // 获得驱动全类名
            path = p.getProperty("path")// 获得数据库连接路径
            user = p.getProperty("user")// 获得数据库登陆名
            psw = p.getProperty("password")// 获得数据库登陆密码
            port = p.getProperty("port").toInt()// 服务器端口
            Class.forName(driver)// 加载驱动
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取数据库连接器
     */
    fun getConnection(): Connection = DriverManager.getConnection(path, user, psw)

    /**
     * 获取服务器配置的端口
     */
    fun getPort() = port

}