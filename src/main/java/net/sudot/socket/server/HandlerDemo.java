package net.sudot.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HandlerDemo implements Runnable {
    private Socket socket;

    public HandlerDemo(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String socketInfo = socket.getInetAddress() + ":" + socket.getPort();
        try {
            System.out.println("新连接:" + socketInfo);
            InputStream in = socket.getInputStream();
            DataInputStream din = new DataInputStream(in);
            String name = din.readUTF();
            System.out.println("收到请求[" + socketInfo + "]:" + name);
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            String msg = socket + "your name :" + name;
            System.out.println("响应请求[" + socketInfo + "]:" + msg);
            dos.writeUTF(msg);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("关闭连接:[" + socketInfo + "]");
                if (socket != null) { socket.close(); }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
