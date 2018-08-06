package cn.leo.kotlin.db

import cn.leo.aio.service.Client
import cn.leo.business.bean.UserBean
import java.nio.channels.SelectionKey

class UserDao {
    private val mysqlHelper = MysqlHelper()

    /**
     * 注册账号，返回 -1 注册失败，一般是用户姓名已经存在
     */
    fun regUser(user: UserBean): Int {
        return mysqlHelper
                .Insert()
                .into("tb_draw_user")
                .values("null,?,?,?,?,?,?,?",
                        user.userName,
                        "123456",
                        user.userName,
                        0,
                        0,
                        user.icon,
                        user.score
                )
                .execute()
    }

    /**
     * 登录
     */
    fun login(key: Client, name: String): UserBean? {
        val resultSet = mysqlHelper.Query()
                .select("*")
                .from("tb_draw_user")
                .where("name = ?", name)
                .execute()
        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val icon = resultSet.getInt("icon")
            val score = resultSet.getInt("score")
            val userBean = UserBean(key)
            userBean.userId = id
            userBean.userName = name
            userBean.icon = icon
            userBean.score = score
            return userBean
        }
        resultSet.close()
        mysqlHelper.close()
        return null
    }

    /**
     * 修改昵称和头像
     */
    fun edit(user: UserBean): Int {
        return mysqlHelper.Update("tb_draw_user")
                .set("name = ? , icon = ?", user.userName, user.icon)
                .where("id = ?", user.userId)
                .execute()
    }

}