package com.yk.bike.utils;

import com.yk.bike.constant.Consts;
import com.yk.bike.response.BaseResponse;

public class NullObjectUtils {
    public static boolean isEmptyString(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isNotEmptyString(String s) {
        return !(s == null || "".equals(s));
    }

    public static String emptyString(String s) {
        if (isEmptyString(s))
            return "";
        else
            return s;
    }

    public static boolean isResponseSuccess(BaseResponse baseResponse) {
        return Consts.COMMON_RESPONSE_SUCCESS_MSG.equals(baseResponse.getMsg());
    }
}
