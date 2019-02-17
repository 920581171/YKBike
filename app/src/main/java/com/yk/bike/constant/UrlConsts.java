package com.yk.bike.constant;

public class UrlConsts {
    public static final String HEAD = "http://";
//    public static final String IP = "192.168.43.151";
//    public static final String IP = "192.168.0.112";
//    public static final String IP = "192.168.3.154";
    public static final String IP = "192.168.43.49";
    public static final String PORT = "8080";
    public static final String IPORT = HEAD + IP + ":" + PORT;
//    public static final String IPORT = HEAD + "192.168.3.120" + ":" + PORT;
//        public static final String IPORT = HEAD + "192.168.137.1" + ":" + PORT;
//    public static final String IPORT = HEAD + "192.168.1.232" + ":" + PORT;

    /**
     * file
     */
    public static final String GET_COMMON_DOWNLOAD_AVATAR = "/common/downloadAvatar";//下载头像
    public static final String POST_COMMON_UPLOAD_AVATAR = "/common/uploadAvatar";//上传头像
    public static final String POST_COMMON_GET_SERVICE_TIME = "/common/getServiceTime";//上传头像

    /**
     * userInfo
     */
    public static final String POST_USER_INFO_FIND_ALL_USER_INFO = "/userinfo/userinfo/findAllUserInfo";//查找所有用户信息
    public static final String POST_USER_INFO_FIND_USER_BY_ID = "/userinfo/findUserByUserId";//根据UserId查找用户
    public static final String POST_USER_INFO_FIND_USER_BY_NAME = "/userinfo/findUserByUserName ";//根据UserName查找用户
    public static final String POST_USER_INFO_FIND_USER_BY_PHONE = "/userinfo/findUserByUserPhone";//根据UserName查找用户
    public static final String POST_USER_INFO_REGISTER_USER_BY_PHONE = "/userinfo/registerUserByPhone";//通过手机号注册用户
    public static final String POST_USER_INFO_REGISTER_USER_BY_NAME = "/userinfo/registerUserByName";//通过用户名和密码注册用户
    public static final String POST_USER_INFO_UPDATE_USER_INFO = "/userinfo/updateUserInfo";//更新用户信息
    public static final String POST_USER_INFO_APP_LOGIN = "/userinfo/appLogin";//用户登录
    public static final String POST_USER_INFO_FIND_DEPOSIT = "/userinfo/findDeposit";

    /**
     * bikeInfo
     */
    public static final String POST_BIKE_INFO_ADD_BIKE_INFO = "/bikeinfo/addBikeInfo";//添加自行车信息
    public static final String POST_BIKE_INFO_ALL_BIKE_INFO = "/bikeinfo/findAllBikeInfo";//查找所有自行车信息
    public static final String POST_BIKE_INFO_FIND_BIKE_BY_BIKE_ID = "/bikeinfo/findBikeInfoByBikeId";//根据bikeId查询自行车信息
    public static final String POST_BIKE_INFO_UPDATE_BIKE_INFO = "/bikeinfo/updateBikeInfo";//更新自行车信息
    public static final String POST_BIKE_INFO_UPDATE_BIKE_LOCATION = "/bikeinfo/updateBikeLocation";//更新自行车位置信息
    public static final String POST_BIKE_INFO_UPDATE_BIKE_FIX = "/bikeinfo/updateBikeFix";//更新自行车维修状态
    public static final String POST_BIKE_INFO_DELETE_BIKE_INFO = "/bikeinfo/deleteBikeInfo";

    /**
     * adminInfo
     */
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_ID = "/adminInfo/findAdminByAdminId";//根据adminId查找管理员
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_ACCOUNT = "/adminInfo/findAdminByAdminAccount";
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_NAME = "/adminInfo/findAdminByAdminName";//根据AdminName查找管理员
    public static final String POST_ADMIN_INFO_FIND_ADMIN_BY_PHONE = "/adminInfo/findAdminByAdminPhone";//根据AdminPhone查找管理员
    public static final String POST_ADMIN_INFO_FIND_ALL_ADMIN_INFO = "/adminInfo/findAllAdminInfo";//查找所有管理员信息
    public static final String POST_ADMIN_INFO_REGISTER_ADMIN_INFO = "/adminInfo/registerAdminInfo";//添加管理员
    public static final String POST_ADMIN_INFO_UPDATE_INFO = "/adminInfo/updateAdminInfo";//更新管理员信息
    public static final String POST_ADMIN_INFO_APP_ADMIN_LOGIN = "/adminInfo/appAdminLogin";//管理员登录

    /**
     * bikeRecord
     */
    public static final String POST_BIKE_RECORD_FIND_BY_ORDER_ID = "/bikeRecord/findBikeRecordByOrderId";
    public static final String POST_BIKE_RECORD_FIND_BY_USER_ID = "/bikeRecord/findBikeRecordByUserId";
    public static final String POST_BIKE_RECORD_FIND_BY_BIKE_ID = "/bikeRecord/findBikeRecordByBikeId";
    public static final String POST_BIKE_RECORD_ALL_BIKE_RECORD = "/bikeRecord/findAllBikeRecord";
    public static final String POST_BIKE_RECORD_ADD_BIKE_RECORD = "/bikeRecord/addBikeRecord";
    public static final String POST_BIKE_RECORD_UPDATE_BIKE_RECORD = "/bikeRecord/updateBikeRecord";
    public static final String POST_BIKE_RECORD_DELETE_BIKE_RECORD = "/bikeRecord/deleteBikeRecord";
    public static final String POST_BIKE_RECORD_IS_CYCLING = "/bikeRecord/findBikeRecordIsCycling";
    public static final String POST_BIKE_RECORD_FINISH_BIKE = "/bikeRecord/finishBike";

    /**
     * siteLocation
     */
    public static final String POST_SITE_LOCATION_ADD_SITE = "/siteLocation/addSiteLocation";
    public static final String POST_SITE_LOCATION_DELETE_SITE = "/siteLocation/deleteSiteLocation";
    public static final String POST_SITE_LOCATION_FIND_ALL_SITE = "/siteLocation/findAllSiteLocation";
    public static final String POST_SITE_LOCATION_FIND_BY_SITE_ID = "/siteLocation/findSiteLocationById";
    public static final String POST_SITE_LOCATION_UPDATE_SITE = "/siteLocation/updateSiteLocation";
}
