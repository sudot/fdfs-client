package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 文件删除命令
 * @author sudot on 2017-04-19 0019.
 */
public class StorageDeleteFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件删除测试
     */
    @Test
    public void testStorageDeleteFileCommand() {

        // 上传文件
        StorePath path = uploadDefaultFile();
        logger.debug("文件上传路径:{}", path.getFullPath());

        // 删除文件
        StorageDeleteFileCommand command = new StorageDeleteFileCommand(path.getGroup(), path.getPath());
        executeStoreCmd(command);
        logger.debug("----文件删除成功-----");
    }

}
