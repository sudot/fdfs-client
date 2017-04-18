package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * 文件查询处理
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class StorageQueryFileInfoCommandTest extends StorageCommandTestBase {

    @Test
    public void testStorageQueryFileInfoCommand() {
        // 上传文件
        StorePath path = uploadDefaultFile();

        // 查询文件
        FileInfo fileInfo = executeStoreCmd(new StorageQueryFileInfoCommand(path.getGroup(), path.getPath()));
        assertNotNull(fileInfo);
        logger.debug("----文件查询处理结果-----");
        logger.debug(fileInfo.toString());
        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
    }

}
