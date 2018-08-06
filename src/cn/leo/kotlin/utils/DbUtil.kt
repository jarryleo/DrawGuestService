package cn.leo.kotlin.utils

import java.sql.ResultSet
import java.sql.SQLException

object DbUtil {
    /**
     * 打印数据库指定记录集
     */
    @Throws(SQLException::class)
    fun show(rs: ResultSet) { // 打印指定记录集
        val rsmd = rs.metaData
        val count = rsmd.columnCount
        for (i in 1..count) {
            print(rsmd.getColumnName(i) + "\t")
        }
        println()
        while (rs.next()) {
            for (i in 1..count) {
                print(rs.getObject(i).toString() + "\t")
            }
            println()
        }
    }
}
