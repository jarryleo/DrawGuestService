package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;

import java.nio.channels.SelectionKey;

public class GameGift implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private GameGift() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new GameGift();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        //送礼物
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        UserBean roomPainter = room.getRoomPainter();
        Client painterKey = roomPainter.getClient();
        MsgManager.sendMsg(painterKey, msgBean);
    }
}
