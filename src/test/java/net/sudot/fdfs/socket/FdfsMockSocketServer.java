package net.sudot.fdfs.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟测试服务
 * @author tobato
 */
public class FdfsMockSocketServer extends Thread {

    public final static int PORT = 22122;
    public final static int STORE_PORT = 23000;
    /** 日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(FdfsMockSocketServer.class);
    private ServerSocket serverSocket;
    private boolean stop = false;
    private int index = 0;
    private Map<String, FdfsMockHandler> pool = new HashMap<String, FdfsMockHandler>();

    public void stopServer() {
        LOGGER.debug("[MockServer]Stop FdfsMockSocketServer..");
        for (FdfsMockHandler handler : pool.values()) {
            handler.stopHandler();
        }
        stop = true;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            LOGGER.debug("[MockServer]start mock server for test..{}", serverSocket);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (!stop) {
            Socket socket = null;
            try {
                index++;
                socket = serverSocket.accept(); // 主线程获取客户端连接
                LOGGER.debug("[MockServer]第" + index + "个客户端成功连接！");
                FdfsMockHandler handler = new FdfsMockHandler(socket);
                pool.put("第" + index + "个", handler);
                handler.start(); // 启动线程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
