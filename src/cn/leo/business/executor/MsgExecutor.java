package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;

import java.nio.channels.SelectionKey;

public interface MsgExecutor {
    void executeMsg(Client key, MsgBean msgBean);
}
