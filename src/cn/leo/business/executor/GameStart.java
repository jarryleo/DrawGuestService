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
import cn.leo.business.words.WordControl;

import java.nio.channels.SelectionKey;

public class GameStart implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private GameStart() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new GameStart();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        //开始游戏
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        if (room.getRoomOwner() == user || room.getRoomState() > 0) {
            //房主才能开始游戏
            startSuccess(room);
        } else {
            startFailed(key);
        }
    }

    private void startSuccess(RoomBean room) {
        //初始化房间状态
        room.setRoomState(1);//游戏状态，第一轮
        //返回成功消息(房间json),还要通知房间内其他人更新列表
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.GAME_START_SUC.getCode());
        MsgManager.sendMsgToRoom(room, msg);
    }

    private void startFailed(Client key) {
        //返回失败
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.GAME_START_FAIL.getCode());
        msg.setMsg("只有房主才能开始游戏");
        MsgManager.sendMsg(key, msg);
    }
}
