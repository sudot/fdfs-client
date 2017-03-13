package net.sudot.fdfs.conn;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.GroupState;
import net.sudot.fdfs.exception.FdfsConnectException;
import net.sudot.fdfs.proto.tracker.TrackerListGroupsCommand;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 验证会轮询地址
 * @author tobato
 */
public class TrackerConnectionManagerTest extends FastdfsTestBase {

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(TrackerConnectionManagerTest.class);

    private String[] ips = {"192.168.174.141:22122", "192.168.1.115:22122"};
    private List<String> trackerIpList = Arrays.asList(ips);

    @Test
    public void testConnectionManager() {
        // 初始化
        TrackerConnectionManager manager = new TrackerConnectionManager(createPool());
        manager.setTrackerList(trackerIpList);
        manager.initTracker();
        List<GroupState> list = null;
        // 第一次执行
        try {
            // 连接失败
            list = manager.executeFdfsTrackerCmd(new TrackerListGroupsCommand());
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsConnectException);
        }

        // 第二次执行
        try {
            // 连接失败
            list = manager.executeFdfsTrackerCmd(new TrackerListGroupsCommand());
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsConnectException);
        }

        LOGGER.debug("执行结果{}", list);
    }

    private FdfsConnectionPool createPool() {
        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectTimeout(TestConstants.connectTimeout);
        factory.setSoTimeout(TestConstants.soTimeout);
        return new FdfsConnectionPool(factory);
    }

}
