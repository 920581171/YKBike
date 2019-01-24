package com.yk.bike.constant;

public class UrlConsts {
    public static final String HEAD = "http://";
    public static final String IP = "192.168.43.151";
    public static final String PORT = "8080";
//    public static final String IPORT = HEAD + IP + ":" + PORT;
//    public static final String IPORT = HEAD + "192.168.3.223" + ":" + PORT;
        public static final String IPORT = HEAD + "192.168.137.1" + ":" + PORT;

    /**
     * userInfo
     */
    public static final String GET_USER_INFO_DOWNLOAD_AVATAR = "/userinfo/downloadAvatar";//下载头像
    public static final String POST_USER_INFO_UPLOAD_AVATAR = "/userinfo/uploadAvatar";//上传头像
    public static final String POST_USER_INFO_FIND_ALL_USER_INFO = "/userinfo/userinfo/findAllUserInfo";//查找所有用户信息
    public static final String POST_USER_INFO_FIND_USER_BY_USER_ID = "/userinfo/findUserByUserId";//根据UserId查找用户
    public static final String POST_USER_INFO_FIND_USER_BY_NAME = "/userinfo/findUserByUserName ";//根据UserName查找用户
    public static final String POST_USER_INFO_FIND_USER_BY_PHONE = "/userinfo/findUserByUserPhone";//根据UserName查找用户
    public static final String POST_USER_INFO_REGISTER_USER_BY_PHONE = "/userinfo/registerUserByPhone";//通过手机号注册用户
    public static final String POST_USER_INFO_REGISTER_USER_BY_NAME = "/userinfo/registerUserByName";//通过用户名和密码注册用户
    public static final String POST_USER_INFO_UPDATE_USER_INFO = "/userinfo/updateUserInfo";//更新用户信息
    public static final String POST_USER_INFO_UPDATE_APP_LOGIN = "/userinfo/appLogin";//用户登陆

    /**
     * bikeInfo
     */
    public static final String POST_BIKE_INFO_ADD_BIKE_INFO = "/bikeinfo/addBikeInfo";//添加自行车信息
    public static final String POST_BIKE_INFO_ALL_BIKE_INFO = "/bikeinfo/findAllBikeInfo";//查找所有自行车信息
    public static final String POST_BIKE_INFO_FIND_BIKE_BY_BIKE_ID = "/bikeinfo/findBikeInfoByBikeId";//根据bikeId查询自行车信息
    public static final String POST_BIKE_INFO_UPDATE_BIKE_INFO = "/bikeinfo/updateBikeInfo";//更新自行车信息
    public static final String POST_BIKE_INFO_UPDATE_BIKE_LOCATION = "/bikeinfo/updateBikeLocation";//更新自行车位置信息
    public static final String POST_BIKE_INFO_DELETE_BIKE_INFO = "/bikeinfo/deleteBikeInfo";

    /**
     * adminInfo
     */
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_USER_ID = "/adminInfo/findAdminByUserId";//根据adminId查找管理员
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_USER_NAME= "/adminInfo/findAdminByUserName";//根据AdminName查找管理员
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_USER_PHONE = "/adminInfo/findAdminByUserPhone";//根据AdminPhone查找管理员
    public static final String POST_ADMIN_INFO_FIND_ALL_ADMIN_INFO = "/adminInfo/findAllAdminInfo";//查找所有管理员信息
    public static final String POST_ADMIN_INFO_REGISTER_ADMIN_INFO = "/adminInfo/registerAdminInfo";//添加管理员
    public static final String POST_ADMIN_INFO_UPDATE_INFO = "/adminInfo/updateAdminInfo";//更新管理员信息
    public static final String POST_ADMIN_INFO_APP_ADMIN_LOGIN = "/adminInfo/appAdminLogin";//管理员登陆
}
