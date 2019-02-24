package com.yk.bike.websocket;

import android.content.Intent;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.yk.bike.base.BaseApplication;
import com.yk.bike.constant.Consts;
import com.yk.bike.constant.UrlConsts;
import com.yk.bike.utils.GsonUtils;
import com.yk.bike.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebSocketManager {

    private static final String TAG = "WebSocketManager";

    private static WebSocketManager instance;
    private WebSocket webSocket;

    public static WebSocketManager getInstance() {
        if (instance == null) {
            synchronized (WebSocketManager.class) {
                if (instance == null) {
                    instance = new WebSocketManager();
                }
            }
        }
        return instance;
    }

    private WebSocketManager() {
    }

    public void init(String param) {
        try {
            if (webSocket == null)
                webSocket = new WebSocketFactory().createSocket("ws://" + UrlConsts.IPORT + "/WebSocketHandler/" + param)
                        .addListener(new WebSocketListener())
                        .connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class WebSocketListener extends WebSocketAdapter {
        @Override
        public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
            super.onBinaryMessage(websocket, binary);
            Log.d(TAG, "收到字节：" + binary.length);
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            WebSocketMessage webSocketMessage = GsonUtils.fromJson(text, WebSocketMessage.class);
            if (webSocketMessage.getType() == Consts.WEBSOCKET_TYPE_FORCE_LOGOUT) {
                BaseApplication.getApplication().sendBroadcast(new Intent().setAction(Consts.BR_ACTION_FORCE_LOGOUT));
            } else if (webSocketMessage.getType() == Consts.WEBSOCKET_TYPE_GET_PARAM) {
                WebSocketParam param = new WebSocketParam()
                        .setLoginId(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_ID))
                        .setLoginType(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_TYPE))
                        .setPassword(SharedPreferencesUtils.getString(Consts.SP_STRING_LOGIN_PASSWORD));
                webSocketMessage.setData(GsonUtils.toJson(param));
                sendText(GsonUtils.toJson(webSocketMessage));
            } else if (webSocketMessage.getType() == Consts.WEBSOCKET_TYPE_CHAT) {
                BaseApplication.getApplication().sendBroadcast(new Intent().setAction(Consts.BR_ACTION_CHAT).putExtra(Consts.INTENT_STRING_CHAT, (String) webSocketMessage.getData()));
            }
            Log.d(TAG, "收到文字：" + text);
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers)
                throws Exception {
            super.onConnected(websocket, headers);
            Log.d(TAG, "连接成功");
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception)
                throws Exception {
            super.onConnectError(websocket, exception);
            Log.d(TAG, "连接错误：" + exception.getMessage());
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            Log.d(TAG, "连接关闭");
        }
    }

    public void sendText(String message) {
        if (webSocket != null) {
            webSocket.sendText(message);
            Log.d(TAG, "发送文字：" + message);
        }
    }

    public void sendBinary(byte[] bytes) {
        if (webSocket != null) {
            webSocket.sendBinary(bytes);
            Log.d(TAG, "发送字节：" + bytes.length);
        }
    }

    public void sendClose() {
        if (webSocket != null) {
            webSocket.sendClose();
        }

    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.disconnect();
            webSocket = null;
        }
    }
}
