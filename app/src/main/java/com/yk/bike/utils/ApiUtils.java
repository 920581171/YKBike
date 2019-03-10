package com.yk.bike.utils;

import com.yk.bike.callback.CommonCallback;
import com.yk.bike.callback.OnResponseListener;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.response.AdminInfoListResponse;
import com.yk.bike.response.AdminInfoResponse;
import com.yk.bike.response.BalanceRecordListResponse;
import com.yk.bike.response.BikeInfoListResponse;
import com.yk.bike.response.BikeInfoResponse;
import com.yk.bike.response.BikeRecordListResponse;
import com.yk.bike.response.BikeRecordResponse;
import com.yk.bike.response.ChatMessageListResponse;
import com.yk.bike.response.ChatRoomListResponse;
import com.yk.bike.response.CommonResponse;
import com.yk.bike.response.DepositRecordListResponse;
import com.yk.bike.response.MessageBroadListResponse;
import com.yk.bike.response.MessageBroadResponse;
import com.yk.bike.response.ScoreRecordListResponse;
import com.yk.bike.response.ScoreRecordResponse;
import com.yk.bike.response.SiteLocationListResponse;
import com.yk.bike.response.UserInfoListResponse;
import com.yk.bike.response.UserInfoResponse;
import com.yk.bike.websocket.ChatMessage;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiUtils {
    private static ApiUtils instance;

    private ApiUtils() {
    }

    public static ApiUtils getInstance() {
        if (instance == null) {
            synchronized (ApiUtils.class) {
                if (instance == null) {
                    instance = new ApiUtils();
                }
            }
        }
        return instance;
    }

    private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();

    private void post(RequestBody requestBody, String url, CommonCallback callback) {
        Request request = new Request.Builder().url(UrlConsts.HEADIPORT + url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    private void get(String url, CommonCallback callback) {
        Request request = new Request.Builder().url(UrlConsts.HEADIPORT + url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    /*--------------------------------------------common---------------------------------------------*/

    /**
     * 获取服务器时间
     *
     * @param onResponseListener
     */
    public void getServiceTime(OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();

        post(formBody, UrlConsts.POST_COMMON_GET_SERVICE_TIME, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 上传头像
     *
     * @param file
     * @param id
     * @param onResponseListener
     */
    public void uploadAvatar(File file, String id, OnResponseListener<CommonResponse> onResponseListener) {
        if (!file.exists()) {
            return;
        }

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), body)
                .addFormDataPart("id", id)
                .build();

        post(requestBody, UrlConsts.POST_COMMON_UPLOAD_AVATAR, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /*---------------------------------------userInfo-----------------------------------------*/

    /**
     * 根据用户名注册用户
     *
     * @param userName
     * @param userPassword
     * @param onResponseListener
     */
    public void registerUserByName(String userName, String userPassword, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName", userName)
                .add("userPassword", userPassword)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_REGISTER_USER_BY_NAME, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
    }

    /**
     * 根据手机号注册
     *
     * @param userPhone
     * @param onResponseListener
     */
    public void registerUserByPhone(String userPhone, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", userPhone)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_REGISTER_USER_BY_PHONE, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
    }

    /**
     * 根据用户ID查找
     *
     * @param userId
     * @param onResponseListener
     */
    public void findUserByUserId(String userId, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_FIND_USER_BY_ID, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
    }

    /**
     * 根据用户名查找
     *
     * @param userName
     * @param onResponseListener
     */
    public void findUserByUserName(String userName, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName", userName)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_FIND_USER_BY_NAME, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 根据手机号查找
     *
     * @param userPhone
     * @param onResponseListener
     */
    public void findUserByUserPhone(String userPhone, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", userPhone)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_FIND_USER_BY_PHONE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 登录验证
     *
     * @param userName
     * @param userPassword
     * @param onResponseListener
     */
    public void appLogin(String userName, String userPassword, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userName", userName)
                .add("userPassword", userPassword)
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_APP_LOGIN, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @param onResponseListener
     */
    public void updateUserInfo(UserInfoResponse.UserInfo userInfo, OnResponseListener<UserInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", NullObjectUtils.emptyString(userInfo.getUserId()))
                .add("userName", NullObjectUtils.emptyString(userInfo.getUserName()))
                .add("userPhone", NullObjectUtils.emptyString(userInfo.getUserPhone()))
                .add("userPassword", NullObjectUtils.emptyString(userInfo.getUserPassword()))
                .add("score", String.valueOf(userInfo.getScore()))
                .add("deposit", String.valueOf(userInfo.getDeposit()))
                .add("balance", String.valueOf(userInfo.getBalance()))
                .build();

        post(formBody, UrlConsts.POST_USER_INFO_UPDATE_USER_INFO, new CommonCallback<>(onResponseListener, UserInfoResponse.class));
    }

    /**
     * 查找所有管理员
     *
     * @param onResponseListener
     */
    public void findDeposit(OnResponseListener<UserInfoListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();
        post(formBody, UrlConsts.POST_USER_INFO_FIND_DEPOSIT, new CommonCallback<>(onResponseListener, UserInfoListResponse.class));
    }

    /*-----------------------------------------------AdminInfo-----------------------------------------------*/

    /**
     * 管理员登录验证
     *
     * @param adminAccountOrPhone
     * @param adminPassword
     * @param onResponseListener
     */
    public void appAdminLogin(String adminAccountOrPhone, String adminPassword, OnResponseListener<AdminInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("adminAccountOrPhone", adminAccountOrPhone)
                .add("adminPassword", adminPassword)
                .build();

        post(formBody, UrlConsts.POST_ADMIN_INFO_APP_ADMIN_LOGIN, new CommonCallback<>(onResponseListener, AdminInfoResponse.class));
    }

    /**
     * 更新管理员信息
     *
     * @param adminInfo
     * @param onResponseListener
     */
    public void updateAdminInfo(AdminInfoResponse.AdminInfo adminInfo, OnResponseListener<AdminInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("adminId", NullObjectUtils.emptyString(adminInfo.getAdminId()))
                .add("adminAccount", NullObjectUtils.emptyString(adminInfo.getAdminAccount()))
                .add("adminName", NullObjectUtils.emptyString(adminInfo.getAdminName()))
                .add("adminPhone", NullObjectUtils.emptyString(adminInfo.getAdminPhone()))
                .add("adminPassword", NullObjectUtils.emptyString(adminInfo.getAdminPassword()))
                .build();

        post(formBody, UrlConsts.POST_ADMIN_INFO_UPDATE_INFO, new CommonCallback<>(onResponseListener, AdminInfoResponse.class));
    }

    /**
     * 查找所有管理员
     *
     * @param onResponseListener
     */
    public void findAllAdminInfo(OnResponseListener<AdminInfoListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();
        post(formBody, UrlConsts.POST_ADMIN_INFO_FIND_ALL_ADMIN_INFO, new CommonCallback<>(onResponseListener, AdminInfoListResponse.class));
    }

    /**
     * 根据ID查找管理员
     *
     * @param adminId
     * @param onResponseListener
     */
    public void findAdminByAdminId(String adminId, OnResponseListener<AdminInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("adminId", adminId)
                .build();

        post(formBody, UrlConsts.POST_ADMIN_INFO_FIND_ADMIN_BY_ID, new CommonCallback<>(onResponseListener, AdminInfoResponse.class));
    }

    public void findAdminByAdminPhone(String adminPhone, OnResponseListener<AdminInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("adminPhone", adminPhone)
                .build();

        post(formBody, UrlConsts.POST_ADMIN_INFO_FIND_ADMIN_BY_PHONE, new CommonCallback<>(onResponseListener, AdminInfoResponse.class));
    }

    /*---------------------------------------------BikeInfo---------------------------------------------*/

    /**
     * 根据id查询自行车
     *
     * @param bikeId
     * @param onResponseListener
     */
    public void findBikeByBikeId(String bikeId, OnResponseListener<BikeInfoResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId", bikeId)
                .build();

        post(formBody, UrlConsts.POST_BIKE_INFO_FIND_BIKE_BY_BIKE_ID, new CommonCallback<>(onResponseListener, BikeInfoResponse.class));
    }

    /**
     * 添加自行车信息
     *
     * @param bikeId
     * @param latitude
     * @param longitude
     * @param onResponseListener
     */
    public void addBikeInfo(String bikeId, double latitude, double longitude, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId", bikeId)
                .add("latitude", String.valueOf(latitude))
                .add("longitude", String.valueOf(longitude))
                .build();

        post(formBody, UrlConsts.POST_BIKE_INFO_ADD_BIKE_INFO, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 更新自行车位置
     *
     * @param bikeId
     * @param latitude
     * @param longitude
     * @param onResponseListener
     */
    public void updateBikeLocation(String bikeId, double latitude, double longitude, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId", bikeId)
                .add("latitude", String.valueOf(latitude))
                .add("longitude", String.valueOf(longitude))
                .build();

        post(formBody, UrlConsts.POST_BIKE_INFO_UPDATE_BIKE_LOCATION, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 更新自行车信息
     *
     * @param bikeInfo
     * @param onResponseListener
     */
    public void updateBikeInfo(BikeInfoResponse.BikeInfo bikeInfo, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId", bikeInfo.getBikeId())
                .add("userId", bikeInfo.getUserId())
                .add("latitude", String.valueOf(bikeInfo.getLatitude()))
                .add("longitude", String.valueOf(bikeInfo.getLongitude()))
                .add("mileage", String.valueOf(bikeInfo.getMileage()))
                .add("fix", String.valueOf(bikeInfo.getFix()))
                .build();

        post(formBody, UrlConsts.POST_BIKE_INFO_UPDATE_BIKE_INFO, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    public void updateBikeFix(String bikeId, String fix, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId", bikeId)
                .add("fix", fix)
                .build();

        post(formBody, UrlConsts.POST_BIKE_INFO_UPDATE_BIKE_FIX, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 查找所有自行车信息
     *
     * @param onResponseListener
     */
    public void findAllBikeInfo(OnResponseListener<BikeInfoListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();
        post(formBody, UrlConsts.POST_BIKE_INFO_ALL_BIKE_INFO, new CommonCallback<>(onResponseListener, BikeInfoListResponse.class));
    }

    /**
     * 删除自行车信息
     *
     * @param bikeId
     * @param onResponseListener
     */
    public void deleteBikeInfo(String bikeId, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("bikeId", bikeId)
                .build();

        post(formBody, UrlConsts.POST_BIKE_INFO_DELETE_BIKE_INFO, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /*-------------------------------------------BikeRecord----------------------------------------------------*/

    /**
     * 根据订单id查找记录
     *
     * @param orderId
     * @param onResponseListener
     */
    public void findBikeRecordByOrderId(String orderId, OnResponseListener<BikeRecordResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("orderId", orderId)
                .build();
        post(formBody, UrlConsts.POST_BIKE_RECORD_FIND_BY_ORDER_ID, new CommonCallback<>(onResponseListener, BikeRecordResponse.class));
    }

    public void findBikeRecordIsCycling(String userId, OnResponseListener<BikeRecordResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();
        post(formBody, UrlConsts.POST_BIKE_RECORD_IS_CYCLING, new CommonCallback<>(onResponseListener, BikeRecordResponse.class));
    }

    /**
     * 添加记录
     *
     * @param bikeRecord
     * @param onResponseListener
     */
    public void addBikeRecord(BikeRecordResponse.BikeRecord bikeRecord, OnResponseListener<BikeRecordResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", NullObjectUtils.emptyString(bikeRecord.getUserId()))
                .add("bikeId", NullObjectUtils.emptyString(bikeRecord.getBikeId()))
                .build();
        post(formBody, UrlConsts.POST_BIKE_RECORD_ADD_BIKE_RECORD, new CommonCallback<>(onResponseListener, BikeRecordResponse.class));
    }

    public void updateBikeRecord(BikeRecordResponse.BikeRecord bikeRecord, OnResponseListener<BikeRecordResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("orderId", NullObjectUtils.emptyString(bikeRecord.getOrderId()))
                .add("userId", NullObjectUtils.emptyString(bikeRecord.getUserId()))
                .add("bikeId", NullObjectUtils.emptyString(bikeRecord.getBikeId()))
                .add("charge", String.valueOf(bikeRecord.getCharge()))
                .add("mileage", String.valueOf(bikeRecord.getMileage()))
                .add("orderStatus", String.valueOf(bikeRecord.getOrderStatus()))
                .build();
        post(formBody, UrlConsts.POST_BIKE_RECORD_UPDATE_BIKE_RECORD, new CommonCallback<>(onResponseListener, BikeRecordResponse.class));
    }

    public void finishBike(String orderId, double latitude, double longitude, OnResponseListener<BikeRecordResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("orderId", orderId)
                .add("latitude", String.valueOf(latitude))
                .add("longitude", String.valueOf(longitude))
                .build();
        post(formBody, UrlConsts.POST_BIKE_RECORD_FINISH_BIKE, new CommonCallback<>(onResponseListener, BikeRecordResponse.class));
    }

    /**
     * 查找所有记录
     *
     * @param onResponseListener
     */
    public void findAllBikeRecord(OnResponseListener<BikeRecordListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();
        post(formBody, UrlConsts.POST_BIKE_RECORD_ALL_BIKE_RECORD, new CommonCallback<>(onResponseListener, BikeRecordListResponse.class));
    }

    /*--------------------------------------------------siteLoaction---------------------------------------------------------*/

    /**
     * 查找所有站点
     *
     * @param onResponseListener
     */
    public void findAllSiteLocation(OnResponseListener<SiteLocationListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();
        post(formBody, UrlConsts.POST_SITE_LOCATION_FIND_ALL_SITE, new CommonCallback<>(onResponseListener, SiteLocationListResponse.class));
    }

    public void addSiteLocation(double latitude, double longitude, int radius, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("latitude", String.valueOf(latitude))
                .add("longitude", String.valueOf(longitude))
                .add("radius", String.valueOf(radius))
                .build();
        post(formBody, UrlConsts.POST_SITE_LOCATION_ADD_SITE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 删除自行车信息
     *
     * @param siteId
     * @param onResponseListener
     */
    public void deleteSiteLocation(String siteId, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("siteId", siteId)
                .build();

        post(formBody, UrlConsts.POST_SITE_LOCATION_DELETE_SITE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /*-------------------------------------------------MessageBroid-------------------------------------------------*/

    /**
     * 添加留言（发送者）
     *
     * @param messageBroad
     * @param onResponseListener
     */
    public void addMessageBroad(MessageBroadResponse.MessageBroad messageBroad, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("senderId", messageBroad.getSenderId())
                .add("messageContent", messageBroad.getMessageContent())
                .add("messageType", messageBroad.getMessageType())
                .build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_ADD, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    public void updateMessageBroad(MessageBroadResponse.MessageBroad messageBroad, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("messageId", messageBroad.getMessageId())
                .add("handlerId", messageBroad.getHandlerId())
                .add("handlerName", messageBroad.getHandlerName())
                .add("messageReply", messageBroad.getMessageReply())
                .build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_UPDATE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 查询所有留言板
     *
     * @param onResponseListener
     */
    public void findAllMessageBroad(OnResponseListener<MessageBroadListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder().build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_FIND_ALL, new CommonCallback<>(onResponseListener, MessageBroadListResponse.class));
    }

    /**
     * 根据messageType查询所有留言板
     *
     * @param onResponseListener
     */
    public void findMessageBroadByType(OnResponseListener<MessageBroadListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("messageType", "1")
                .build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_FIND_ALL_BY_TYPE, new CommonCallback<>(onResponseListener, MessageBroadListResponse.class));
    }

    /**
     * 根据发送者Id查询留言板
     *
     * @param senderId
     * @param onResponseListener
     */
    public void findMessageBroadBySenderId(String senderId, OnResponseListener<MessageBroadListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("senderId", senderId)
                .build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_FIND_BY_SENDER_ID, new CommonCallback<>(onResponseListener, MessageBroadListResponse.class));
    }

    /**
     * 根据处理者Id查询留言板
     *
     * @param handlerId
     * @param onResponseListener
     */
    public void findMessageBroadByHandlerIdWithType(String handlerId, OnResponseListener<MessageBroadListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("handlerId", handlerId)
                .add("messageType", "1")
                .build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_FIND_BY_HANDLER_ID_WITH_TYPE, new CommonCallback<>(onResponseListener, MessageBroadListResponse.class));
    }

    /**
     * 根据处理者Id查询留言板
     *
     * @param messageId
     * @param onResponseListener
     */
    public void findMessageBroadByMessageId(String messageId, OnResponseListener<MessageBroadResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("messageId", messageId)
                .build();

        post(formBody, UrlConsts.POST_MESSAGE_BROAD_FIND_BY_MESSAGE_ID, new CommonCallback<>(onResponseListener, MessageBroadResponse.class));
    }

    /*-------------------------------------------------------------chatMessage-----------------------------------------------------------*/

    /**
     * 根据fristId查找聊天人员
     */
    public void findALLChatMessageByBothId(String id1, String id2, OnResponseListener<ChatMessageListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("id1", id1)
                .add("id2", id2)
                .build();

        post(formBody, UrlConsts.POST_CHAT_MESSAGE_BOTH_ALL, new CommonCallback<>(onResponseListener, ChatMessageListResponse.class));
    }

    /*-------------------------------------------------------------chatRoom-----------------------------------------------------------*/


    public void findALLChatRoomByFristId(String fristId, OnResponseListener<ChatRoomListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("fristId", fristId)
                .build();

        post(formBody, UrlConsts.POST_CHAT_ROOM_FIND_ALL_BY_FRISTID, new CommonCallback<>(onResponseListener, ChatRoomListResponse.class));
    }

    /*-----------------------------------------------depositrecord-----------------------------------------------------------*/

    /**
     * 添加押金记录
     */
    public void addDepositRecord(String userId, float deposit, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("deposit", String.valueOf(deposit))
                .build();

        post(formBody, UrlConsts.POST_DEPOSIT_RECORD_ADD, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 更新押金记录
     */
    public void updateDepositRecord(String userId, String depositStatus, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_DEPOSIT_RECORD_UPDATE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 根据用户Id查询押金记录
     *
     * @param userId
     * @param onResponseListener
     */
    public void findDepositRecordByUserId(String userId, OnResponseListener<DepositRecordListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_DEPOSIT_RECORD_BY_USERID, new CommonCallback<>(onResponseListener, DepositRecordListResponse.class));
    }

    /*-----------------------------------------------depositrecord-----------------------------------------------------------*/

    /**
     * 添加余额记录
     */
    public void addBalanceRecord(String userId, float balance, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("balance", String.valueOf(balance))
                .build();

        post(formBody, UrlConsts.POST_BALANCE_RECORD_ADD, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 更新余额记录
     */
    public void updateBalanceRecord(String userId, String depositStatus, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_BALANCE_RECORD_UPDATE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 根据用户Id查询余额记录
     *
     * @param userId
     * @param onResponseListener
     */
    public void findBalanceRecordByUserId(String userId, OnResponseListener<BalanceRecordListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_BALANCE_RECORD_BY_USERID, new CommonCallback<>(onResponseListener, BalanceRecordListResponse.class));
    }

    /*-----------------------------------------------scorerecord-----------------------------------------------------------*/

    /**
     * 添加积分记录
     */
    public void addScoreRecord(String userId, int score, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("score", String.valueOf(score))
                .build();

        post(formBody, UrlConsts.POST_SCORE_RECORD_ADD, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 更新积分记录
     */
    public void updateScoreRecord(String userId, String depositStatus, OnResponseListener<CommonResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_SCORE_RECORD_UPDATE, new CommonCallback<>(onResponseListener, CommonResponse.class));
    }

    /**
     * 根据用户Id查询积分记录
     *
     * @param userId
     * @param onResponseListener
     */
    public void findScoreRecordByUserId(String userId, OnResponseListener<ScoreRecordListResponse> onResponseListener) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        post(formBody, UrlConsts.POST_SCORE_RECORD_BY_USERID, new CommonCallback<>(onResponseListener, ScoreRecordListResponse.class));
    }
}
