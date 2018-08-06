package cn.leo.business.factory;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.executor.*;

public class GameExecutorFactory {

    public static void executeMsg(Client key, MsgBean msgBean) {
        getMsgExecutor(msgBean.getCode()).executeMsg(key, msgBean);
    }

    private static MsgExecutor getMsgExecutor(int msgCode) {
        if (msgCode == MsgCode.ROOM_LIST.getCode()) {
            return RoomList.getInstance();
        } else if (msgCode == MsgCode.ROOM_CREATE.getCode()) {
            return RoomCreate.getInstance();
        } else if (msgCode == MsgCode.ROOM_JOIN.getCode()) {
            return RoomJoin.getInstance();
        } else if (msgCode == MsgCode.ROOM_EXIT.getCode()) {
            return RoomExit.getInstance();
        } else if (msgCode == MsgCode.ROOM_INFO.getCode()) {
            return RoomInfo.getInstance();
        } else if (msgCode == MsgCode.GAME_START.getCode()) {
            return GameStart.getInstance();
        } else if (msgCode == MsgCode.GAME_CHAT.getCode()) {
            return GameChat.getInstance();
        } else if (msgCode == MsgCode.GAME_GIFT_FLOWER.getCode() ||
                msgCode == MsgCode.GAME_GIFT_SLIPPER.getCode()) {
            return GameGift.getInstance();
        }
        return MsgError.getInstance();
    }
}
