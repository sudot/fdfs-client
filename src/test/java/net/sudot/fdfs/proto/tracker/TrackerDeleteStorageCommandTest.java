package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.proto.ErrorCodeConstants;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 删除存储服务器
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class TrackerDeleteStorageCommandTest extends StorageCommandTestBase {

    /**
     * 删除存储服务器
     */
    @Test
    public void testTrackerDeleteStorageCommand() {

        // 获取存储节点信息
        StorageNode client = executeTrackerCmd(new TrackerGetStoreStorageCommand(TestConstants.DEFAULT_GROUP));
        logger.debug("-----列举存储服务器状态处理结果-----");
        logger.debug(client.toString());

        // 获取源服务器
        TrackerDeleteStorageCommand command = new TrackerDeleteStorageCommand(client.getGroupName(), client.getIp());
        try {
            executeTrackerCmd(command);
        } catch (Exception e) {
            assertTrue(e instanceof FdfsServerException);
            assertTrue(((FdfsServerException) e).getErrorCode() == ErrorCodeConstants.ERR_NO_EBUSY);
        }
        logger.debug("----删除存储服务器-----");
    }

}
