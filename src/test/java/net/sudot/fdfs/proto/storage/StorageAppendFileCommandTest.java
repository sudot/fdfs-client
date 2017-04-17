package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

/**
 * 文件续传命令
 * @author tobato
 */
public class StorageAppendFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件续传需要先使用 append模式Save一个可以续传的文件
     * 然后才能使用续传命令续传文件
     * @throws IOException
     */
    @Test
    public void testStorageAppendFileCommand() throws IOException {
        String firstText = "Tobato is a good man.他是一个好人\r\n";
        InputStream firstIn = getTextInputStream(firstText);
        long firstSize = firstIn.available();
        // 先上载第一段文字
        StorePath path = uploadInputStream(firstIn, "txt", firstSize, true);
        logger.debug(path.getFullPath());
        // 添加第二段文字
        String secendText = "Work hard and hard. 努力工作啊\r\n";
        InputStream secendIn = getTextInputStream(secendText);
        long secendSize = secendIn.available();
        // 文件续传
        execStorageAppendFileCommand(secendIn, secendSize, path.getPath());
        logger.debug(path.getFullPath());
        byte[] bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));
        logger.debug(new String(bytes));

        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
        firstIn.close();
        secendIn.close();
    }

    /**
     * 文件续传操作
     */
    public void execStorageAppendFileCommand(InputStream in, long fileSize, String path) {
        StorageAppendFileCommand command = new StorageAppendFileCommand(in, fileSize, path);
        executeStoreCmd(command);
        assertNotNull(path);
        logger.debug("--文件续传操作结果-----");
    }

}
