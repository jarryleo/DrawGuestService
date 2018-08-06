package cn.leo.business.bean;

import cn.leo.utils.JsonUtil;

public class MsgBean {
    private String msg; // 消息内容
    private String senderName; //发消息的人
    private int id; //消息id
    private int type; // 消息类型
    private int code; // 消息错误码
    private int area; // 房间号 如果有群或者房间的话
    private long time; // 消息时间

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
