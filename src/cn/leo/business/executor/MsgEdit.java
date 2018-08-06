package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;
import cn.leo.kotlin.db.UserDao;
import cn.leo.utils.JsonUtil;
import cn.leo.utils.Logger;

public class MsgEdit implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgEdit() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgEdit();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        String json = msgBean.getMsg();
        Logger.d("edit:" + json);
        UserBean userBean = JsonUtil.fromJson(json, UserBean.class);
        //数据库查询用户是否存在
        UserDao dao = new UserDao();
        int edit = 0;
        if (userBean != null) {
            edit = dao.edit(userBean);
            Logger.d("edit:" + userBean.toString());
        }
        //返回修改信息
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.SYS.getType());
        if (edit > 0) {
            //修改成功
            UserBean user = UserManager.getUser(key);
            user.setIcon(userBean.getIcon());
            user.setUserName(userBean.getUserName());
            //返回修改成功的消息
            msg.setCode(MsgCode.EDIT_SUC.getCode());
        } else {
            //修改失败
            msg.setCode(MsgCode.EDIT_FAI.getCode());
        }
        MsgManager.sendMsg(key, msg);
    }
}
