package com.xzl.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by sqwu on 2019/11/14
 */
public class XZLWebSocketClient extends WebSocketClient {

    private XZLWebSocketListener mXZLWebSocketListener;

    public XZLWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (mXZLWebSocketListener != null){
            mXZLWebSocketListener.onOpen(handshakedata);
        }
    }

    @Override
    public void onMessage(String message) {
        if (mXZLWebSocketListener != null){
            mXZLWebSocketListener.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (mXZLWebSocketListener != null){
            mXZLWebSocketListener.onClose(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception ex) {
        if (mXZLWebSocketListener != null){
            mXZLWebSocketListener.onError(ex);
        }
    }

    public void setClientListener(XZLWebSocketListener listener){
        mXZLWebSocketListener = listener;
    }
}
