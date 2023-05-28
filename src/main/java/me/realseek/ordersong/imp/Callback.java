package me.realseek.ordersong.imp;

import java.io.IOException;

public interface Callback {
    void callbackMethod() throws IOException, InterruptedException;
}