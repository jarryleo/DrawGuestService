package cn.leo.business.factory;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgType;
import cn.leo.business.executor.*;

public class MsgExecutorFactory {

    public static void executeMsg(Client key, MsgBean msgBean) {
        getMsgExecutor(msgBean.getType()).executeMsg(key, msgBean);
    }

    private static MsgExecutor getMsgExecutor(int msgType) {
        if (msgType == MsgType.SYS.getType()) {
            return MsgSystem.getInstance();
        } else if (msgType == MsgType.REG.getType()) {
            return MsgReg.getInstance();
        } else if (msgType == MsgType.EDIT.getType()) {
            return MsgEdit.getInstance();
        } else if (msgType == MsgType.LOGIN.getType()) {
            return MsgLogin.getInstance();
        } else if (msgType == MsgType.GAME.getType()) {
            return MsgGame.getInstance();
        } else if (msgType == MsgType.PAINT.getType()) {
            return MsgPaint.getInstance();
        } else if (msgType == MsgType.PRI.getType()) {
            return MsgPri.getInstance();
        } else if (msgType == MsgType.PUB.getType()) {
            return MsgPub.getInstance();
        }
        return MsgError.getInstance();
    }
}
