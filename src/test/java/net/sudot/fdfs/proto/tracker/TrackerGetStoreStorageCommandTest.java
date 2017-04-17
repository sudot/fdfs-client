package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.proto.CommandTestBase;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * 获取存储节点交易
 * @author tobato
 */
public class TrackerGetStoreStorageCommandTest extends CommandTestBase {

    @Test
    public void testTrackerGetStoreStorageCommand() {
        StorageNode client = executeTrackerCmd(new TrackerGetStoreStorageCommand());
        assertNotNull(client.getInetSocketAddress());
        logger.debug("-----获取存储节点交易处理结果-----{}");
        logger.debug(client.toString());

        // Connection conn = new
        // DefaultConnection(client.getInetSocketAddress(), 500, 300, null);
        // logger.debug("连接状态{}", conn.isValid());
        // conn.close();
    }

    @Test
    public void testTrackerGetStoreStorageWithGroupCommand() {
        StorageNode client = executeTrackerCmd(new TrackerGetStoreStorageCommand("group1"));
        assertNotNull(client.getInetSocketAddress());
        logger.debug("-----按组获取存储节点交易处理结果-----");
        logger.debug(client.toString());

        // Connection conn = new
        // DefaultConnection(client.getInetSocketAddress(), 500, 300, null);
        // logger.debug("连接状态{}", conn.isValid());
        // conn.close();
    }

}
