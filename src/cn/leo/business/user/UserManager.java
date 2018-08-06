package cn.leo.business.user;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.UserBean;
import cn.leo.business.executor.RoomExit;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private static ConcurrentHashMap<Client, UserBean> mUsers = new ConcurrentHashMap<>();

    /**
     * 添加连接到管理类
     *
     * @param user
     */
    public static void addUser(Client key, UserBean user) {
        mUsers.put(key, user);
    }

    /**
     * 移除连接
     *
     * @param key
     */
    public static void removeUser(Client key) {
        //从房间移除
        RoomExit.getInstance().executeMsg(key, null);
        mUsers.remove(key);
    }

    /**
     * 获取所有在线连接
     *
     * @return
     */
    public static Set<Client> getUsers() {
        return mUsers.keySet();
    }

    /**
     * 根据连接获取用户
     *
     * @param key
     * @return
     */
    public static UserBean getUser(Client key) {
        return mUsers.get(key);
    }

}
