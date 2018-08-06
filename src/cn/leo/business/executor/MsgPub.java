package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.message.MsgManager;

public class MsgPub implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgPub() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgPub();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        //UserBean user = UserManager.getUser(key);
        MsgManager.sendMsgToAll(msgBean);
    }
}
