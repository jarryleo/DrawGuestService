package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;

public class RoomExit implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private RoomExit() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new RoomExit();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        if (room != null) {
            room.removeUser(user);
            MsgBean msg = new MsgBean();
            msg.setType(MsgType.GAME.getType());
            msg.setCode(MsgCode.ROOM_INFO.getCode());
            msg.setMsg(room.toString());
            MsgManager.sendMsgToRoom(user, msg, true);
        }
    }
}
