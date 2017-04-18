package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件截取命令
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class StorageTruncateCommandTest extends StorageCommandTestBase {

    @Test
    public void testStorageTruncateCommandText() throws IOException {
        String text = "这是一段测试文字";
        InputStream firstIn = getTextInputStream(text);
        long firstSize = firstIn.available();
        logger.debug("上载文字:{}", firstSize);
        StorePath path = uploadInputStream(firstIn, "txt", firstSize, true);
        byte[] bytes = null;
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));
        logger.debug("远程文件内容:{}", new String(bytes));

        // 文件删减
        executeStoreCmd(new StorageTruncateCommand(path.getPath(), "这".getBytes(TestConstants.DEFAULT_CHARSET).length));
        logger.debug("--文件处理成功--");
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));
        logger.debug("删减后远程文件内容:{}", new String(bytes));

        // 文件删减
        executeStoreCmd(new StorageTruncateCommand(path.getPath(), 0));
        logger.debug("--文件处理成功--");
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));
        logger.debug("删减后远程文件内容:{}", new String(bytes));
        // 文件清理
        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
    }

}
