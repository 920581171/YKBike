package com.yk.bike.utils;

import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;

public class SpUtils {
    public static String getIpAddress() {
        return (String) SharedPreferencesUtils.get(Consts.SP_STRING_IP_ADDRESS, UrlConsts.IPS[0]);
    }
    public static String getLoginId() {
        return SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID);
    }

    public static String getLoginType() {
        return SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE);
    }

    public static String getLoginName() {
        return SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_NAME);
    }

    public static String getLoginPhone() {
        return SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_PHONE);
    }

    public static boolean isLoginAdmin(){
        return Consts.LOGIN_TYPE_ADMIN.equals(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE));
    }
}
