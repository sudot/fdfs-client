package net.sudot.fdfs.conn;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.GroupState;
import net.sudot.fdfs.proto.tracker.TrackerListGroupsCommand;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 验证会轮询地址
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class TrackerConnectionManagerTest extends FastdfsTestBase {

    private List<String> trackerIpList = Arrays.asList(TestConstants.TRACKER_LIST.split(TrackerConnectionManager.SPLIT_REGEX));

    @Test
    public void testConnectionManager() {
        // 初始化
        TrackerConnectionManager manager = new TrackerConnectionManager(createPool());
        manager.setTrackerList(trackerIpList);
        List<GroupState> list = null;
        // 第一次执行
        list = manager.executeFdfsTrackerCmd(new TrackerListGroupsCommand());
        logger.debug("执行结果{}", list);

        // 第二次执行
        list = manager.executeFdfsTrackerCmd(new TrackerListGroupsCommand());
        logger.debug("执行结果{}", list);
    }

    private FdfsConnectionPool createPool() {
        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectTimeout(TestConstants.CONNECT_TIMEOUT);
        factory.setSoTimeout(TestConstants.SO_TIMEOUT);
        return new FdfsConnectionPool(factory);
    }

}
