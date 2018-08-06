package cn.leo.business.message;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.room.RoomManager;
import cn.leo.business.words.WordControl;

import java.util.List;

public class GameControl extends Thread {
    private static final int INTERVAL = 1000;
    private static final MsgBean GameMsg = new MsgBean();

    private GameControl() {
        super();
        GameMsg.setType(MsgType.GAME.getType());
        GameMsg.setCode(MsgCode.ROOM_INFO.getCode());
    }

    /**
     * 启动游戏流程控制
     */
    public static void startGameControl() {
        new GameControl().start();
    }

    @Override
    public void run() {
        for (; ; ) {
            checkRooms();
            try {
                sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检测房间流程
     */
    private void checkRooms() {
        List<RoomBean> rooms = RoomManager.getRooms();
        if (rooms.size() > 0) {
            //遍历所有房间
            for (RoomBean room : rooms) {
                //找到游戏中的房间
                if (room.getRoomState() > 0) {
                    //获取倒计时时间
                    int countDown = room.getPaintCountDown();
                    //倒计时
                    if (countDown > 0) {
                        room.countDown();
                    }
                    //游戏时间归零，换人
                    if (countDown == 0) {
                        String word = WordControl.getWord();
                        String[] w = word.split(",");
                        room.setWord(w[0]); //游戏答案
                        room.setWordTips(w[1]); //游戏提示
                        room.setPaintCountDown(85); //游戏倒计时
                        room.nextPaint();
                        //发送消息给房间所有人
                        GameMsg.setMsg(room.toString());
                        MsgManager.sendMsgToRoom(room, GameMsg);
                    }
                }
            }
        }
    }
}
