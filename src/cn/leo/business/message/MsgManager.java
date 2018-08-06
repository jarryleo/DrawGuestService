package cn.leo.business.message;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.factory.MsgExecutorFactory;
import cn.leo.business.user.UserManager;
import cn.leo.utils.JsonUtil;
import cn.leo.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MsgManager {
    private final static int TIME_OUT = 30_000;// 超时时间  毫秒

    /**
     * 发送消息到单一对象
     *
     * @param key
     * @param msg
     */
    public static void sendMsg(Client key, MsgBean msg) {
        String info = msg.toString();
        try {
            key.send(info.getBytes("utf-8"),(short)0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给所有连接服务器的客户端群发消息
     *
     * @param msg
     */
    public static void sendMsgToAll(MsgBean msg) {
        Set<Client> users = UserManager.getUsers();
        for (Client key : users) {
            // 发送消息
            sendMsg(key, msg);
        }
    }

    /**
     * 发送消息给房间内用户
     *
     * @param userBean     发消息的用户
     * @param msgBean      要转发的消息
     * @param exceptSender 不转发给发消息的用户
     */
    public static void sendMsgToRoom(UserBean userBean, MsgBean msgBean, boolean exceptSender) {
        RoomBean room = userBean.getRoom();
        if (room == null) return;
        List<UserBean> users = room.getUsers();
        for (UserBean user : users) {
            //不发给发消息的人
            if (user == userBean && exceptSender) {
                continue;
            }
            sendMsg(user.getClient(), msgBean);
        }
    }

    /**
     * 发送消息给一个房间的所有人
     *
     * @param room    房间
     * @param msgBean 消息
     */
    public static void sendMsgToRoom(RoomBean room, MsgBean msgBean) {
        List<UserBean> users = room.getUsers();
        for (UserBean user : users) {
            sendMsg(user.getClient(), msgBean);
        }
    }

    /**
     * 处理客户端发来的信息
     *
     * @param key
     * @param msgJson
     */
    public static void processMsg(Client key, String msgJson) {
        MsgBean msgBean = null;
        //判断消息是否合法
        msgBean = JsonUtil.fromJson(msgJson, MsgBean.class);
        if (msgBean == null) {
            InterceptConnection(key, "非法连接");
            return;
        }
        //消息分发策略
        MsgExecutorFactory.executeMsg(key, msgBean);
    }

    /**
     * 转发画画数据
     *
     * @param key
     * @param paint
     */
    public static void processPaint(Client key, byte[] paint) {
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        for (UserBean u : room.getUsers()) {
            if (u != user) {
                u.sendMsg(paint);
            }
        }
    }


    public static void checkHeart() {
        Set<Client> users = UserManager.getUsers();
        Iterator<Client> iterator = users.iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Client selectionKey = iterator.next();
            UserBean user = UserManager.getUser(selectionKey);
            if (/*TextUtil.isEmpty(user.getUserName()) &&*/
                    currentTime - user.getConnectTime() > TIME_OUT) {
                // 超时没有登录的链接剔除
                InterceptConnection(selectionKey, "心跳超时");
            }
        }
    }

    /**
     * 断开指定链接，当有非法连接出现时
     *
     * @param client
     */
    public static void InterceptConnection(Client client, String errorMsg) {
        Logger.i("断开链接[" + client.getIp() + "] - " + errorMsg);
        UserManager.removeUser(client);
        Logger.d("clientCount:" + UserManager.getUsers().size());
        client.close();
    }
}
