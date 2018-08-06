package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;

public class MsgPri implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgPri() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgPri();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        UserBean user = UserManager.getUser(key);
        MsgManager.sendMsgToRoom(user, msgBean, false);
    }
}
