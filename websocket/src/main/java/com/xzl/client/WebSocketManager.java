package com.xzl.client;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by sqwu on 2019/11/14
 */
public class WebSocketManager {
    private static volatile WebSocketManager mWebSocketManager = null;

    private static XZLWebSocketClient sXZLWebSocketClient = null;
    private XZLWebSocketListener mXZLWebSocketListener;
    private URI mUri;


    private WebSocketManager(){}

    public static WebSocketManager getInstance(){
        if (mWebSocketManager == null){
            synchronized (WebSocketManager.class){
                if (mWebSocketManager == null){
                    mWebSocketManager = new WebSocketManager();
                }
            }
        }
        return mWebSocketManager;
    }

    public void connect(){
        if (sXZLWebSocketClient == null){
            if (mUri == null){
                throw new NullPointerException("url 未设置，请通过setUrl设置");
            }
            sXZLWebSocketClient = new XZLWebSocketClient(mUri);
            sXZLWebSocketClient.setClientListener(mXZLWebSocketListener);
        }

        try {
            sXZLWebSocketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void setUrl(String url){
        try {
            mUri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        if (sXZLWebSocketClient != null &&sXZLWebSocketClient.isOpen()){
            sXZLWebSocketClient.send(msg);
        }
    }

    public void disconnect(){
        try {
            if (sXZLWebSocketClient != null &&sXZLWebSocketClient.isOpen()){
                sXZLWebSocketClient.close();

            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            sXZLWebSocketClient = null;
        }

    }

    public void setWebSocketListener(XZLWebSocketListener xzlWebSocketListener){
        this.mXZLWebSocketListener = xzlWebSocketListener;
    }

}
