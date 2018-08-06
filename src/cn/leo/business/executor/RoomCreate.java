package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.room.RoomManager;
import cn.leo.business.user.UserManager;

public class RoomCreate implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private RoomCreate() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new RoomCreate();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        //创建房间成功
        UserBean user = UserManager.getUser(key);
        RoomBean room = RoomManager.createRoom(user);
        //返回创建成功消息(房间json)
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.ROOM_CREATE_SUC.getCode());
        msg.setMsg(room.toString());
        MsgManager.sendMsg(key, msg);
        //给所有没有房间的玩家发送
        msg.setCode(MsgCode.ROOM_LIST.getCode());
        msg.setMsg(RoomManager.getRoomListJson());
        for (Client selectionKey : UserManager.getUsers()) {
            if (UserManager.getUser(selectionKey).getRoom() == null) {
                MsgManager.sendMsg(selectionKey, msg);
            }
        }
    }
}
