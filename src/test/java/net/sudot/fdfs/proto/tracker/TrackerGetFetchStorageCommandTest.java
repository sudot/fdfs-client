package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.domain.StorageNodeInfo;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 获取源服务器
 * @author tobato
 */
public class TrackerGetFetchStorageCommandTest extends StorageCommandTestBase {

    /**
     * 获取源服务器
     */
    @Test
    public void testTrackerGetFetchStorageCommand() {

        // 上传文件
        StorePath path = uploadDefaultFile();

        // 获取源服务器
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(path.getGroup(), path.getPath(),
                false);
        StorageNodeInfo client = executeTrackerCmd(command);
        logger.debug("----获取源服务器-----");
        logger.debug(client.toString());
    }
}
