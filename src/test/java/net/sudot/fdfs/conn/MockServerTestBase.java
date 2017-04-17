package net.sudot.fdfs.conn;

import net.sudot.fdfs.TestConstants;
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

    protected static FdfsMockSocketServer SOCKET_SERVER = new FdfsMockSocketServer();
    /** 日志 */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public InetSocketAddress address = TestConstants.TRACKER_ADDRESS;

    @BeforeClass
    public static void startMockServer() {
        SOCKET_SERVER.start();
    }

    @AfterClass
    public static void stopMockServer() {
        SOCKET_SERVER.stopServer();
    }

}
