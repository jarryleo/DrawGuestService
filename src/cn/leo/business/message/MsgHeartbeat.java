package cn.leo.business.message;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgType;
import cn.leo.business.constant.MsgCode;

public class MsgHeartbeat extends Thread {
    private static final int INTERVAL = 1000 * 10; // 10秒
    private static final MsgBean SYSMSG = new MsgBean();

    private MsgHeartbeat() {
        super();
        SYSMSG.setType(MsgType.SYS.getType());
        SYSMSG.setCode(MsgCode.HEART.getCode());
    }

    /**
     * 启动心跳检测机制
     */
    public static void startHeartbeat() {
        new MsgHeartbeat().start();
    }

    @Override
    public void run() {
        for (; ; ) {
            //SYSMSG.setTime(System.currentTimeMillis());
            checkHeart();
            try {
                sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送心跳信息
     */
    private void sendHeartMsg() {
        //MsgManager.sendMsgToAll(SYSMSG);
    }

    /**
     * 检测心跳
     */
    private void checkHeart() {
        MsgManager.checkHeart();
    }
}
