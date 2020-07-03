package com.xzl.client;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by sqwu on 2019/11/14
 */
public interface XZLWebSocketListener {
     void onOpen(ServerHandshake handshakedata) ;

     void onMessage(String message) ;

     void onClose(int code, String reason, boolean remote);

     void onError(Exception ex);
}
