package me.realseek.imp;

import java.io.IOException;

public interface Callback {
    void callbackMethod() throws IOException, InterruptedException;
}