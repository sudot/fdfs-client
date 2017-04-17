package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 文件下载
 * @author tobato
 */
public class StorageDownloadCommandTest extends StorageCommandTestBase {

    @Test
    public void testStorageDownloadCommand() {
        // 上传文件
        StorePath path = uploadDefaultFile();
        DownloadFileWriter callback = new DownloadFileWriter("Test.jpg");
        // 删除文件
        StorageDownloadCommand<String> command = new StorageDownloadCommand<String>(path.getGroup(), path.getPath(),
                callback);
        String fileName = executeStoreCmd(command);
        logger.debug("----文件下载成功-----{}", fileName);
    }

}
