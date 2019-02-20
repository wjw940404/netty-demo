package com.jerry.netty.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞SocketServer
 * @author Jerry.Wu
 * @description:
 * @date 2019/2/20 10:35
 */
public class IOServer {

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();  // 调用accept方法，这里会阻塞当前线程，等待socket连接，有新连接才会向下执行
                    System.out.println("有新的socket连接");
                    new Thread(() -> {  // 当有一个新的socket连接时，开启一个新线程
                        try {
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            while (true) {
                                int len;
                                while ((len = inputStream.read(data)) != -1) {
                                    System.out.println(new String(data, 0, len));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("线程结束了.....");
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
