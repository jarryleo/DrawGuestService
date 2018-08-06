package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.factory.GameExecutorFactory;

public class MsgGame implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgGame() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgGame();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        GameExecutorFactory.executeMsg(key, msgBean);
    }
}
