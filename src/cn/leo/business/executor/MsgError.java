package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.message.MsgManager;

public class MsgError implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgError() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgError();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        MsgManager.InterceptConnection(key, "错误消息类型");
    }
}
