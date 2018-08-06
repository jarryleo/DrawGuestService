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

public class RoomJoin implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private RoomJoin() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new RoomJoin();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        try {
            int roomId = Integer.parseInt(msgBean.getMsg());
            //加入房间
            UserBean user = UserManager.getUser(key);
            RoomBean roomBean = RoomManager.getRoomWithId(roomId);
            if (roomBean != null) {
                roomBean.addUser(user);
                joinSuccess(user, roomBean);
            } else {
                joinFailed(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            joinFailed(key);
        }
    }

    private void joinSuccess(UserBean user, RoomBean roomBean) {
        //返回加入成功消息(房间json),还要通知房间内其他人更新列表
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.ROOM_JOIN_SUC.getCode());
        msg.setMsg(roomBean.toString());
        MsgManager.sendMsgToRoom(roomBean, msg);
    }

    private void joinFailed(Client key) {
        //返回加入失败
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.ROOM_JOIN_FAI.getCode());
        MsgManager.sendMsg(key, msg);
    }
}
