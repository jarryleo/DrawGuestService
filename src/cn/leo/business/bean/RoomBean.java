package cn.leo.business.bean;

import cn.leo.business.room.RoomManager;
import cn.leo.utils.JsonUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomBean {
    //房间内的玩家列表
    private List<UserBean> mUsers = Collections.synchronizedList(new ArrayList<UserBean>());
    //房间id
    private int mRoomId;
    //房间正在画画的词汇
    private String mWord = "";
    //画画的词汇提示语
    private String mWordTips = "";
    //房间状态，0为未开始游戏，1-n为进行到第n轮
    private int mRoomState;
    //答对题目的人
    private int answerRightCount;
    //房主
    @JSONField(serialize = false)
    private transient UserBean mRoomOwner;
    //当前房间绘画人
    @JSONField(serialize = false)
    private transient UserBean mRoomPainter;
    //当前画画倒计时
    private int mPaintCountDown;

    public int getRoomId() {
        return mRoomId;
    }

    public void setRoomId(int mRoomId) {
        this.mRoomId = mRoomId;
    }

    public int getRoomState() {
        return mRoomState;
    }

    public void setRoomState(int roomState) {
        mRoomState = roomState;
    }

    public UserBean getRoomOwner() {
        return mRoomOwner;
    }

    public void setRoomOwner(UserBean roomOwner) {
        mRoomOwner = roomOwner;
    }

    public UserBean getRoomPainter() {
        return mRoomPainter;
    }

    public void setRoomPainter(UserBean roomPainter) {
        mRoomPainter = roomPainter;
    }

    public int getPaintCountDown() {
        return mPaintCountDown;
    }

    public void setPaintCountDown(int paintCountDown) {
        mPaintCountDown = paintCountDown;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getWordTips() {
        return mWordTips;
    }

    public void setWordTips(String wordTips) {
        mWordTips = wordTips;
    }

    public int getAnswerRightCount() {
        return answerRightCount;
    }

    public void setAnswerRightCount(int answerRightCount) {
        this.answerRightCount = answerRightCount;
    }

    //玩家进入房间
    public void addUser(UserBean user) {
        if (user == null) return;
        if (mUsers.contains(user)) {
            mUsers.remove(user);
        }
        mUsers.add(user);
        user.setRoom(this);
    }

    //玩家退出房间
    public void removeUser(UserBean user) {
        mUsers.remove(user);
        //user.setRoom(null);
        //如果房间没人了
        if (getUserCount() == 0) {
            //从服务器删除房间
            RoomManager.removeRoom(this);
            return;
        }
        //如果是房主退出
        if (mRoomOwner == user) {
            //房主转给列表第一个人
            mRoomOwner = mUsers.get(0);
            return;
        }
        if (mRoomPainter == user) {
            //如果是画画的人退出
            //画画权交给另一个人 or 倒计时继续进行，直到时间耗尽自动转移 TODO

            return;
        }

    }

    /**
     * 计时
     */
    public void countDown() {
        if (mPaintCountDown > 0) {
            mPaintCountDown--;
        }
    }
    //获取房间内所有人列表

    public List<UserBean> getUsers() {
        return mUsers;
    }

    //获取房间总人数

    public int getUserCount() {
        return mUsers.size();
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    //下一个画画
    public void nextPaint() {
        int i = mUsers.indexOf(mRoomPainter);
        if (i >= mUsers.size() - 1) {
            i = 0;
            answerRightCount = 0;
            mRoomState++;
        } else {
            i++;
        }
        mRoomPainter = mUsers.get(i);
    }
}
