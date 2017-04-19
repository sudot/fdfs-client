package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.proto.CommandTestBase;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * 获取存储节点交易
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class TrackerGetStoreStorageCommandTest extends CommandTestBase {

    @Test
    public void testTrackerGetStoreStorageCommand() {
        StorageNode client = executeTrackerCmd(new TrackerGetStoreStorageCommand());
        assertNotNull(client.getInetSocketAddress());
        logger.debug("-----获取存储节点交易处理结果-----{}");
        logger.debug(client.toString());
    }

    @Test
    public void testTrackerGetStoreStorageWithGroupCommand() {
        StorageNode client = executeTrackerCmd(new TrackerGetStoreStorageCommand(TestConstants.DEFAULT_GROUP));
        assertNotNull(client.getInetSocketAddress());
        logger.debug("-----按组获取存储节点交易处理结果-----");
        logger.debug(client.toString());
    }

}
