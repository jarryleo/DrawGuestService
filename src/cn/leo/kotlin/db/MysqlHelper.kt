package cn.leo.kotlin.db

import cn.leo.kotlin.utils.PropertiesUtil
import cn.leo.utils.Logger
import java.sql.PreparedStatement
import java.sql.ResultSet

class MysqlHelper {
    private val conn = PropertiesUtil.getConnection()
    private var ps: PreparedStatement? = null

    fun close() {
        try {
            ps?.close()
            conn.close()
        } catch (e: Exception) {

        }
    }

    inner class Query {
        //"select * from person where username = ? and password = ?"
        private val sb = StringBuilder()
        private var args: Array<out Any>? = null
        fun select(columns: String): Query {
            sb.append("select $columns ")
            return this
        }

        fun from(table: String): Query {
            sb.append("from $table ")
            return this
        }

        fun where(clause: String, vararg args: Any): Query {
            this.args = args
            sb.append("where $clause ")
            return this
        }

        fun limit(n: Int): Query {
            sb.append("limit $n ")
            return this
        }

        fun offset(m: Int): Query {
            sb.append("offset $m ")
            return this
        }

        fun execute(): ResultSet {
            val sql = sb.toString()
            ps = conn.prepareStatement(sql)
            args?.forEachIndexed { index, any ->
                when (any) {
                    is Int -> {
                        ps?.setInt(index + 1, any)
                    }
                    is String -> {
                        ps?.setString(index + 1, any)
                    }
                }
            }
            return ps?.executeQuery()!!
        }
    }

    inner class Insert {
        //"insert into person values(null,?,?,?,?,?)"
        private val sb = StringBuilder()
        private var args: Array<out Any>? = null
        fun into(table: String): Insert {
            sb.append("insert into $table ")
            return this
        }

        fun values(columns: String, vararg values: Any): Insert {
            this.args = values
            sb.append("values($columns)")
            return this
        }

        fun execute(): Int {
            val sql = sb.toString()
            ps = conn.prepareStatement(sql)
            args?.forEachIndexed { index, any ->
                when (any) {
                    is Int -> {
                        ps?.setInt(index + 1, any)
                    }
                    is String -> {
                        ps?.setString(index + 1, any)
                    }
                }
            }
            return try {
                ps?.executeUpdate()!!
            } catch (e: Exception) {
                -1
            } finally {
                close()
            }
        }

    }

    inner class Update(table: String) {
        //"update person set password = ?,name = ? where name = ?"
        private val sb = StringBuilder("update $table ")
        private var args: List<Any>? = ArrayList()

        fun set(columns: String, vararg values: Any): Update {
            this.args = this.args?.plus(values)
            sb.append("set $columns ")
            return this
        }

        fun where(clause: String, vararg args: Any): Update {
            this.args = this.args?.plus(args)
            sb.append("where $clause ")
            return this
        }

        fun execute(): Int {
            val sql = sb.toString()
            ps = conn.prepareStatement(sql)
            args?.forEachIndexed { index, any ->
                when (any) {
                    is Int -> {
                        ps?.setInt(index + 1, any)
                    }
                    is String -> {
                        ps?.setString(index + 1, any)
                    }
                }
            }
            val update = ps?.executeUpdate()
            close()
            return update!!
        }
    }

    inner class Delete {
        //"delete from person where name = ?"
        private val sb = StringBuilder()
        private var args: Array<out Any>? = null
        fun from(table: String): Delete {
            sb.append("delete from $table ")
            return this
        }

        fun where(clause: String, vararg args: Any): Delete {
            this.args = args
            sb.append("where $clause ")
            return this
        }

        fun execute(): Int {
            val sql = sb.toString()
            ps = conn.prepareStatement(sql)
            args?.forEachIndexed { index, any ->
                when (any) {
                    is Int -> {
                        ps?.setInt(index + 1, any)
                    }
                    is String -> {
                        ps?.setString(index + 1, any)
                    }
                }
            }
            val delete = ps?.executeUpdate()
            close()
            return delete!!
        }
    }
}