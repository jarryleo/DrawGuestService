package cn.leo.business.constant;

public enum MsgType {
    SYS(0),//系统消息
    PUB(1),//群聊
    PRI(2),//私聊
    REG(3),//注册
    EDIT(4),//修改
    LOGIN(5),//登录
    PAINT(6),//画画
    GAME(7);//游戏
    private int type;

    MsgType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
