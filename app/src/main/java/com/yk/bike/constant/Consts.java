package com.yk.bike.constant;

public class Consts {
    public static final int COMMON_RESPONSE_SUCCESS_CODE = 1;
    public static final int COMMON_RESPONSE_ERROR_CODE = -1;
    public static final String COMMON_RESPONSE_SUCCESS_MSG = "SUCCESS";
    public static final String COMMON_RESPONSE_ERROR_MSG = "ERROR";
    public static final String SEARCH_USER_RESPONSE_ERROR_MSG = "用户已存在";

    public static final int REQUEST_CODE_SCAN = 0;//二维码
    public static final int REQUEST_CODE_ACCOUNT = 1;//用户信息管理
    public static final int REQUEST_CODE_FROM_CAMERA = 100;
    public static final int REQUEST_CODE_FROM_ALBUM = 101;

    public static final int RESULT_CODE_LOGOUT = 10;//退出

    public static final String BR_ACTION_EXIT = "com.yk.bike.action.exit";
    public static final String BR_ACTION_LOGIN = "com.yk.bike.action.login";
    public static final String BR_ACTION_LOGOUT = "com.yk,bike.action.logout";
    public static final String BR_ACTION_CHAT = "com.yk,bike.action.chat";
    public static final String BR_ACTION_FORCE_LOGOUT = "com.yk,bike.action.force.logout";

    public static final String SP_STRING_IP_ADDRESS = "ipAddress";
    public static final String SP_STRING_LOGIN_NAME = "loginName";
    public static final String SP_STRING_LOGIN_PHONE = "loginPhone";
    public static final String SP_STRING_LOGIN_PASSWORD = "loginPassword";
    public static final String SP_STRING_LOGIN_TYPE = "loginType";
    public static final String SP_STRING_LOGIN_ID = "loginId";
    public static final String SP_STRING_LOGIN_GET_CODE_TIME = "getCodeTime";//获取验证码时间
    public static final String SP_STRING_ORDER_ID = "orderId";
    public static final String SP_STRING_MINE_FASTEST = "mineFastest";

    public static final String LOGIN_TYPE_PHONE = "typePhone";
    public static final String LOGIN_TYPE_USER = "typeUser";
    public static final String LOGIN_TYPE_ADMIN = "typeAdmin";

    public static final String CODE_RESULT_TYPE_ADMIN_ADDED = "adminAdded";
    public static final String CODE_RESULT_TYPE_ADMIN_NEW_ADD = "adminNewAdd";
    public static final String CODE_RESULT_TYPE_ADMIN_FIX = "adminFix";
    public static final String CODE_RESULT_TYPE_USER_NO_BIKE = "userNoBike";
    public static final String CODE_RESULT_TYPE_USER_BIKE = "userBike";
    public static final String CODE_RESULT_TYPE_USER_FIX = "userFix";

    public static final int WEBSOCKET_TYPE_FORCE_LOGOUT = -1;
    public static final int WEBSOCKET_TYPE_GET_PARAM = 0;
    public static final int WEBSOCKET_TYPE_CHAT = 1;
    public static final int WEBSOCKET_TYPE_LOCATION = 2;
    public static final int WEBSOCKET_TYPE_STOP_LOCATION = 3;

    public static final String INTENT_STRING_MESSAGE_ID = "messageId";
    public static final String INTENT_STRING_TO_ID = "toId";
    public static final String INTENT_STRING_CHAT = "chat";

    public static final String CHAT_TYPE_TEXT = "0";
    public static final String CHAT_TYPE_IMAGE = "1";
    public static final String CHAT_TYPE_AUDIO = "2";

    public static final int NOTIFICATION_ID_CHAT = 1;
    public static final int NOTIFICATION_ID_OTHER = 0;

    public static final String CHANNEL_ID_CHAT = "1";
    public static final String CHANNEL_ID_OTHER = "0";

    public static final String CHANNEL_NAME_CHAT = "聊天消息";
    public static final String CHANNEL_NAME_OTHER = "其他";
}
