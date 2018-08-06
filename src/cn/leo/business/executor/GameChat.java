package cn.leo.business.executor;

import cn.leo.aio.service.Client;
import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;

public class GameChat implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private GameChat() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new GameChat();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(Client key, MsgBean msgBean) {
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        if (room == null) return;
        //聊天信息
        String msg = msgBean.getMsg();
        String word = room.getWord();
        //判断是否答对
        if (user != room.getRoomPainter()) {
            if (msg.equals(word)) {
                //答对加分
                int i = user.getScore() + 1;
                user.setScore(i);
                int answerRightCount = room.getAnswerRightCount();
                room.setAnswerRightCount(answerRightCount + 1);
                msgBean.setMsg("猜中答案！");
                msgBean.setCode(100);
                //数据库加分 TODO

                //如果全部答对，下一个开始
                int count = room.getAnswerRightCount();
                if (count == room.getUserCount() - 1) {
                    room.setPaintCountDown(5);
                    msgBean.setId(200);
                }
                //发送房间信息
                /*MsgBean msg2 = new MsgBean();
                msg2.setType(MsgType.GAME.getType());
                msg2.setCode(MsgCode.ROOM_INFO.getCode());
                msg2.setMsg(room.toString());
                MsgManager.sendMsg(key, msg2);*/


            }
        } else {
            //判断是否泄题
            char[] chars = word.toCharArray();
            for (char c : chars) {
                msg = msg.replace(c, '*');
            }
            msgBean.setMsg(msg);
        }
        //转发给房间内所有人
        msgBean.setSenderName(user.getUserName());
        MsgManager.sendMsgToRoom(room, msgBean);
    }
}
