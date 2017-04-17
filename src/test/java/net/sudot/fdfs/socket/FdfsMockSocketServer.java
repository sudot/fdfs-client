package net.sudot.fdfs.socket;

import net.sudot.fdfs.TestConstants;
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

    /** 日志 */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ServerSocket serverSocket;
    private boolean stop = false;
    private int index = 0;
    private Map<String, FdfsMockHandler> pool = new HashMap<String, FdfsMockHandler>();

    public void stopServer() {
        logger.debug("[MockServer]Stop FdfsMockSocketServer..");
        for (FdfsMockHandler handler : pool.values()) {
            handler.stopHandler();
        }
        stop = true;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(TestConstants.TRACKER_PORT);
            logger.debug("[MockServer]start mock server for test..{}", serverSocket);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (!stop) {
            Socket socket = null;
            try {
                index++;
                socket = serverSocket.accept(); // 主线程获取客户端连接
                logger.debug("[MockServer]第" + index + "个客户端成功连接！");
                FdfsMockHandler handler = new FdfsMockHandler(socket);
                pool.put("第" + index + "个", handler);
                handler.start(); // 启动线程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
