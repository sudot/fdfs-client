package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import java.io.File;

/**
 * 文件下载
 * @author tobato
 */
public class StorageDownloadCommandTest extends StorageCommandTestBase {

    @Test
    public void testStorageDownloadCommand() {
        // 上传文件
        StorePath path = uploadDefaultFile();
        // 下载文件
        DownloadFileWriter callback = new DownloadFileWriter("Test.jpg");
        StorageDownloadCommand<String> command = new StorageDownloadCommand<String>(path.getGroup(), path.getPath(), callback);
        String fileName = executeStoreCmd(command);
        logger.debug("----文件下载成功-----{}", fileName);
        // 删除文件
        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
        boolean delete = new File(fileName).delete();
        logger.debug("删除文件:{}", delete);
    }

}
