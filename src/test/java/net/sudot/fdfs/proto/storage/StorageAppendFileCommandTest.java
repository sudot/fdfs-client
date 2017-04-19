package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件续传命令
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class StorageAppendFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件续传需要先使用 append模式Save一个可以续传的文件
     * 然后才能使用续传命令续传文件
     * @throws IOException
     */
    @Test
    public void testStorageAppendFileCommand() throws IOException {
        String firstText = "testStorageAppendFileCommand.一个单元测试\r\n";
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
        executeStoreCmd(new StorageAppendFileCommand(secendIn, secendSize, path.getPath()));
        logger.debug(path.getFullPath());
        byte[] bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));

        // 文件清理
        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));

        String context = new String(bytes);
        logger.debug("续传后的内容:{}", context);
        Assert.assertEquals("内容不一致", firstText + secendText, context);
    }
}
