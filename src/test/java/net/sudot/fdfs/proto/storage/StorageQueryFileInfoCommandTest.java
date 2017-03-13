package net.sudot.fdfs.proto.storage;

import static org.junit.Assert.assertNotNull;

import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 文件查询处理
 * 
 * @author tobato
 *
 */
public class StorageQueryFileInfoCommandTest  extends StorageCommandTestBase {

    @Test
    public void testStorageQueryFileInfoCommand() {
        // 上传文件
        StorePath path = uploadDefaultFile();

        // 查询文件
        StorageQueryFileInfoCommand command = new StorageQueryFileInfoCommand(path.getGroup(), path.getPath());
        FileInfo fileInfo = executeStoreCmd(command);
        assertNotNull(fileInfo);
        LOGGER.debug("----文件查询处理结果-----");
        LOGGER.debug(fileInfo.toString());
    }

}