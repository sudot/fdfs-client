package net.sudot.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientDemo {

    public static void main (String[] args) throws Exception {
        Socket s = new Socket("127.0.0.1", 22122);

        OutputStream out = s.getOutputStream();

        DataOutputStream dout = new DataOutputStream(out);
        dout.writeUTF("oftenlin");

        InputStream in = s.getInputStream();
        DataInputStream din = new DataInputStream(in);

        String st = din.readUTF();
        System.out.println("收到响应:" + st);

        in.close();
        System.out.println("连接是否关闭:" + s.isClosed());
        out.close();
        s.close();
    }
}
