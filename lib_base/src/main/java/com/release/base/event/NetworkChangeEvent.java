package com.release.base.event;

/**
 * @author Mr.release
 * @create 2019/8/2
 * @Describe
 */
public class NetworkChangeEvent {

    boolean isConnected;

    public NetworkChangeEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

}
