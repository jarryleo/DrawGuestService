package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;

public class MsgSystem implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgSystem() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgSystem();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        //更新心跳
        if (msgBean.getCode() == MsgCode.HEART.getCode()) {
            UserBean user = UserManager.getUser(key);
            if (user != null) {
                MsgManager.sendMsg(key, msgBean);
            }
        }
    }
}
