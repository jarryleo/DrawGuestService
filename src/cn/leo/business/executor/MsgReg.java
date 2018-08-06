package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.kotlin.db.UserDao;
import cn.leo.utils.JsonUtil;

public class MsgReg implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgReg() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgReg();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        String json = msgBean.getMsg();
        UserBean userBean = JsonUtil.fromJson(json, UserBean.class);
        //查询数据库是否可以添加
        UserDao dao = new UserDao();
        int i = -1;
        if (userBean != null) {
            i = dao.regUser(userBean);
        }
        //返回注册信息
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.SYS.getType());
        if (i == -1) {
            //注册失败
            msg.setCode(MsgCode.REG_FAI.getCode());
        } else {
            //注册成功
            msg.setCode(MsgCode.REG_SUC.getCode());
        }
        MsgManager.sendMsg(key, msg);
    }
}
