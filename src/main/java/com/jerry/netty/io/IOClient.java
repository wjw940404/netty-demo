package com.jerry.netty.io;

import java.net.Socket;
import java.util.Date;

/**
 * @author Jerry.Wu
 * @description:
 * @date 2019/2/20 10:35
 */
public class IOClient {

    public static void main(String args[]) {
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {
                    Socket socket = new Socket("127.0.0.1", 8000);
                    socket.getOutputStream().write((new Date() + "：" + i).getBytes());
                    i++;
                    socket.getOutputStream().flush();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
