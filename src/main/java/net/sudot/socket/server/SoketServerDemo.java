package net.sudot.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SoketServerDemo {
    private int port = 22122;
    private ServerSocket serverSocket;

    public static void main() throws Exception {
        SoketServerDemo server = new SoketServerDemo();
        System.out.println("------服务启动--------");
        server.service();
    }

    public void service() {
        int i = 0;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (true) {
            Socket socket = null;
            try {
                i++;
                socket = serverSocket.accept(); // 主线程获取客户端连接
                System.out.println("第" + i + "个客户端成功连接！");
                Thread workThread = new Thread(new HandlerDemo(socket)); // 创建线程
                workThread.start(); // 启动线程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
