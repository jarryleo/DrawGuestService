package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.room.RoomManager;

public class RoomList implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private RoomList() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new RoomList();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.ROOM_LIST.getCode());
        msg.setMsg(RoomManager.getRoomListJson());
        MsgManager.sendMsg(key, msg);
    }
}
