package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorageState;
import net.sudot.fdfs.proto.CommandTestBase;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 列举存储目录服务器状态
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class TrackerListStoragesCommandTest extends CommandTestBase {

    @Test
    public void testGroupAndIp() {
        List<StorageState> list = executeTrackerCmd(
                new TrackerListStoragesCommand(TestConstants.DEFAULT_GROUP, TestConstants.STORE_ADDRESS.getHostString()));
        assertTrue(list.size() > 0);
        logger.debug("-----根据IP列举存储服务器状态处理结果-----");
        logger.debug(list.toString());
    }

    @Test
    public void testGroup() {
        List<StorageState> list = executeTrackerCmd(new TrackerListStoragesCommand(TestConstants.DEFAULT_GROUP));
        assertTrue(list.size() > 0);
        logger.debug("-----列举存储服务器状态处理结果-----");
        logger.debug(list.toString());
    }

}
