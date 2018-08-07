package cn.leo;

import cn.leo.aio.service.Client;
import cn.leo.aio.service.Service;
import cn.leo.aio.service.ServiceListener;
import cn.leo.business.bean.UserBean;
import cn.leo.business.message.GameControl;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;
import cn.leo.kotlin.utils.PropertiesUtil;
import cn.leo.utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;

public class Main implements ServiceListener {

    public static void main(String[] args) {
        Service.INSTANCE.start(PropertiesUtil.INSTANCE.getPort(), new Main());
        GameControl.startGameControl();// 开启游戏流程控制器
    }

    @Override
    public void onNewConnectComing(@NotNull Client client) {
        UserBean user = new UserBean(client);
        UserManager.addUser(client, user);
    }

    @Override
    public void onConnectInterrupt(@NotNull Client client) {
        removeConnect(client);
    }

    @Override
    public void onDataArrived(@NotNull Client client, @NotNull byte[] data, short cmd) {
        String msg = null;
        try {
            if (data.length == 0) return;
            String s = new String(data, 0, 1, "utf-8");
            if ("P".equals(s)) {
                MsgManager.processPaint(client, data);
            } else {
                msg = new String(data, "utf-8");
                //Logger.d("收到" + client.getIp() + "消息:" + msg);
                MsgManager.processMsg(client, msg);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 移除异常连接
    public static synchronized void removeConnect(Client key) {
        UserBean user = UserManager.getUser(key);
        if (user == null || key == null)
            return;
        UserManager.removeUser(key);
    }
}
