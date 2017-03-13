package net.sudot.fdfs.conn;

import net.sudot.fdfs.socket.FdfsMockSocketServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * socket 测试基类
 * @author tobato
 */
public class MockServerTestBase {

    public static final int soTimeout = 350;
    public static final int connectTimeout = 50;
    protected static FdfsMockSocketServer socketServer = new FdfsMockSocketServer();
    /** 日志 */
    protected final Logger LOGGER = LoggerFactory.getLogger(MockServerTestBase.class);
    public InetSocketAddress address = new InetSocketAddress(FdfsMockSocketServer.PORT);

    @BeforeClass
    public static void startMockServer() {
        socketServer.start();
    }

    @AfterClass
    public static void stopMockServer() {
        socketServer.stopServer();
    }

}
