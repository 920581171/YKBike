package com.yk.bike.constant;

public class Consts {
    public static final int COMMON_RESPONSE_SUCCESS_CODE = 1;
    public static final int COMMON_RESPONSE_ERROR_CODE = -1;
    public static final String COMMON_RESPONSE_SUCCESS_MSG = "SUCCESS";
    public static final String COMMON_RESPONSE_ERROR_MSG = "ERROR";
    public static final String SEARCH_USER_RESPONSE_ERROR_MSG = "用户已存在";

    public static final String BR_ACTION_EXIT = "com.yk.bike.action.exit";
    public static final String BR_ACTION_USER_LOGIN = "com.yk.bike.action.userLogin";
    public static final String BR_ACTION_ADMIN_LOGIN = "com.yk.bike.action.adminLogin";

    public static final String SP_LOGIN_NAME = "loginName";
    public static final String SP_LOGIN_PASSWORD = "loginPassword";
    public static final String SP_LOGIN_TYPE = "loginType";

    public static final String LOGIN_TYPE_PHONE = "typePhone";
    public static final String LOGIN_TYPE_USER = "typeUser";
    public static final String LOGIN_TYPE_ADMIN = "typeAdmin";
}
